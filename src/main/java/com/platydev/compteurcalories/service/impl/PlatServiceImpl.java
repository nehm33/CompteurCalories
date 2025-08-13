package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.input.RecetteInputDTO;
import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.entity.Recette;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.PlatMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.PlatRepository;
import com.platydev.compteurcalories.repository.RecetteRepository;
import com.platydev.compteurcalories.service.PlatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlatServiceImpl implements PlatService {

    private final PlatRepository platRepository;
    private final AlimentRepository alimentRepository;
    private final RecetteRepository recetteRepository;
    private final PlatMapper platMapper;

    @Autowired
    public PlatServiceImpl(PlatRepository platRepository, AlimentRepository alimentRepository,
                           RecetteRepository recetteRepository, PlatMapper platMapper) {
        this.platRepository = platRepository;
        this.alimentRepository = alimentRepository;
        this.recetteRepository = recetteRepository;
        this.platMapper = platMapper;
    }

    @Override
    public PlatResponse find(Pageable pageable, String search) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Plat> platsPage;

        if (search != null && !search.trim().isEmpty()) {
            // Recherche avec filtre de nom pour l'utilisateur connecté
            platsPage = platRepository.findByAlimentUserIdAndAlimentNomContainingIgnoreCase(
                    currentUser.getId(), search.trim(), pageable);
        } else {
            // Récupérer tous les plats de l'utilisateur connecté
            platsPage = platRepository.findByAlimentUserId(currentUser.getId(), pageable);
        }

        // Utiliser le mapper pour convertir les entités en DTOs
        List<PlatWithoutRecetteDTO> platDTOs = platMapper.toPlatWithoutRecetteDTOList(platsPage.getContent());

        // Créer la réponse paginée avec le mapper
        return platMapper.toPlatResponse(platDTOs, platsPage);
    }

    @Override
    public PlatDTO findById(long platId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());
        return platMapper.toDTO(plat);
    }

    @Override
    public void add(@Valid PlatInputDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Validation et agrégation des recettes
        Map<Aliment, Float> alimentsQuantites = validateAndAggregateRecettes(platDTO, currentUser);

        // Création de l'aliment représentant le plat avec calcul des valeurs nutritionnelles
        Aliment alimentPlat = createAlimentForPlat(platDTO, alimentsQuantites, currentUser);

        // Sauvegarde de l'aliment (cascade vers le plat)
        Aliment savedAliment = alimentRepository.save(alimentPlat);

        // Création et sauvegarde des recettes
        saveRecettes(savedAliment.getPlat(), alimentsQuantites);
    }

    @Override
    public void update(long platId, @Valid PlatInputDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat platExistant = findPlatAndCheckOwnership(platId, currentUser.getId());

        // Validation et agrégation des recettes
        Map<Aliment, Float> nouvellesRecettes = validateAndAggregateRecettes(platDTO, currentUser);

        // Récupération des recettes existantes du plat
        List<Recette> recettesExistantes = recetteRepository.findAllByRecetteId_PlatId(platId);

        // Comparaison des recettes : même aliments avec mêmes quantités ?
        if (areRecettesIdentical(recettesExistantes, nouvellesRecettes)) {
            // Mise à jour simple : uniquement les données du plat (nom, nbPortions)
            updatePlatDataOnly(platExistant, platDTO, nouvellesRecettes, currentUser);
        } else {
            // Mise à jour complète : suppression des anciennes recettes et recréation
            updatePlatWithNewRecettes(platExistant, platDTO, nouvellesRecettes, currentUser);
        }
    }

    @Override
    public void delete(long platId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());

        alimentRepository.deleteById(plat.getAliment().getId());
    }

    /**
     * Valide et agrège les recettes d'un PlatInputDTO
     * Méthode commune utilisée par add() et update()
     * Note: Les validations de base (non null, > 0) sont maintenant gérées par les annotations @Valid
     */
    private Map<Aliment, Float> validateAndAggregateRecettes(PlatInputDTO platDTO, User currentUser) {
        // Agrégation des recettes par Aliment avec vérification des accès
        Map<Aliment, Float> alimentsQuantites = new HashMap<>();

        for (RecetteInputDTO recetteDTO : platDTO.recettes()) {
            // Récupération de l'aliment
            Aliment aliment = alimentRepository.findById(recetteDTO.alimentId())
                    .orElseThrow(() -> new NotFoundException("Aliment avec l'ID " + recetteDTO.alimentId() + " non trouvé"));

            // Vérifier que l'utilisateur a accès à cet aliment
            if (!aliment.getUser().equals(currentUser) && aliment.getUser().getId() != 1L) {
                throw new ForbiddenException("Vous n'avez pas accès à l'aliment avec l'ID " + recetteDTO.alimentId());
            }

            // Vérifier que ce n'est pas un plat (on ne peut pas mettre un plat dans un plat)
            if (aliment.getPlat() != null) {
                throw new ApiException("Impossible d'utiliser un plat comme ingrédient d'un autre plat (ID: " + recetteDTO.alimentId() + ")");
            }

            // Agrégation : additionner les quantités si l'aliment existe déjà
            alimentsQuantites.merge(aliment, recetteDTO.quantite(), Float::sum);
        }

        return alimentsQuantites;
    }

    /**
     * Sauvegarde les recettes associées à un plat
     * Méthode commune utilisée par add() et updatePlatWithNewRecettes()
     */
    private void saveRecettes(Plat plat, Map<Aliment, Float> alimentsQuantites) {
        List<Recette> recettes = new ArrayList<>();
        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            Aliment alimentRecette = entry.getKey();
            Float quantiteTotale = entry.getValue();

            Recette recette = new Recette(plat, alimentRecette, quantiteTotale);
            recettes.add(recette);
        }

        recetteRepository.saveAll(recettes);
    }

    /**
     * Calcule les valeurs nutritionnelles d'un plat à partir de ses ingrédients
     * Méthode commune pour les calculs nutritionnels
     */
    private NutrientTotals calculateNutrientTotals(Map<Aliment, Float> alimentsQuantites) {
        NutrientTotals totals = new NutrientTotals();

        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            Aliment aliment = entry.getKey();
            float quantite = entry.getValue();

            totals.calories += safeMultiply(aliment.getCalories(), quantite / 100f);
            totals.matieresGrasses += safeMultiply(aliment.getMatieresGrasses(), quantite / 100f);
            totals.matieresGrassesSatures += safeMultiply(aliment.getMatieresGrassesSatures(), quantite / 100f);
            totals.matieresGrassesMonoInsaturees += safeMultiply(aliment.getMatieresGrassesMonoInsaturees(), quantite / 100f);
            totals.matieresGrassesPolyInsaturees += safeMultiply(aliment.getMatieresGrassesPolyInsaturees(), quantite / 100f);
            totals.matieresGrassesTrans += safeMultiply(aliment.getMatieresGrassesTrans(), quantite / 100f);
            totals.proteines += safeMultiply(aliment.getProteines(), quantite / 100f);
            totals.glucides += safeMultiply(aliment.getGlucides(), quantite / 100f);
            totals.sucre += safeMultiply(aliment.getSucre(), quantite / 100f);
            totals.fibres += safeMultiply(aliment.getFibres(), quantite / 100f);
            totals.sel += safeMultiply(aliment.getSel(), quantite / 100f);
            totals.cholesterol += safeMultiply(aliment.getCholesterol(), quantite / 100f);
            totals.provitamineA += safeMultiply(aliment.getProvitamineA(), quantite / 100f);
            totals.vitamineA += safeMultiply(aliment.getVitamineA(), quantite / 100f);
            totals.vitamineB1 += safeMultiply(aliment.getVitamineB1(), quantite / 100f);
            totals.vitamineB2 += safeMultiply(aliment.getVitamineB2(), quantite / 100f);
            totals.vitamineB3 += safeMultiply(aliment.getVitamineB3(), quantite / 100f);
            totals.vitamineB5 += safeMultiply(aliment.getVitamineB5(), quantite / 100f);
            totals.vitamineB6 += safeMultiply(aliment.getVitamineB6(), quantite / 100f);
            totals.vitamineB8 += safeMultiply(aliment.getVitamineB8(), quantite / 100f);
            totals.vitamineB9 += safeMultiply(aliment.getVitamineB9(), quantite / 100f);
            totals.vitamineB11 += safeMultiply(aliment.getVitamineB11(), quantite / 100f);
            totals.vitamineB12 += safeMultiply(aliment.getVitamineB12(), quantite / 100f);
            totals.vitamineC += safeMultiply(aliment.getVitamineC(), quantite / 100f);
            totals.vitamineD += safeMultiply(aliment.getVitamineD(), quantite / 100f);
            totals.vitamineE += safeMultiply(aliment.getVitamineE(), quantite / 100f);
            totals.vitamineK1 += safeMultiply(aliment.getVitamineK1(), quantite / 100f);
            totals.vitamineK2 += safeMultiply(aliment.getVitamineK2(), quantite / 100f);
            totals.ars += safeMultiply(aliment.getArs(), quantite / 100f);
            totals.b += safeMultiply(aliment.getB(), quantite / 100f);
            totals.ca += safeMultiply(aliment.getCa(), quantite / 100f);
            totals.cl += safeMultiply(aliment.getCl(), quantite / 100f);
            totals.choline += safeMultiply(aliment.getCholine(), quantite / 100f);
            totals.cr += safeMultiply(aliment.getCr(), quantite / 100f);
            totals.co += safeMultiply(aliment.getCo(), quantite / 100f);
            totals.cu += safeMultiply(aliment.getCu(), quantite / 100f);
            totals.fe += safeMultiply(aliment.getFe(), quantite / 100f);
            totals.f += safeMultiply(aliment.getF(), quantite / 100f);
            totals.i += safeMultiply(aliment.getI(), quantite / 100f);
            totals.mg += safeMultiply(aliment.getMg(), quantite / 100f);
            totals.mn += safeMultiply(aliment.getMn(), quantite / 100f);
            totals.mo += safeMultiply(aliment.getMo(), quantite / 100f);
            totals.na += safeMultiply(aliment.getNa(), quantite / 100f);
            totals.p += safeMultiply(aliment.getP(), quantite / 100f);
            totals.k += safeMultiply(aliment.getK(), quantite / 100f);
            totals.rb += safeMultiply(aliment.getRb(), quantite / 100f);
            totals.sio += safeMultiply(aliment.getSiO(), quantite / 100f);
            totals.s += safeMultiply(aliment.getS(), quantite / 100f);
            totals.se += safeMultiply(aliment.getSe(), quantite / 100f);
            totals.v += safeMultiply(aliment.getV(), quantite / 100f);
            totals.sn += safeMultiply(aliment.getSn(), quantite / 100f);
            totals.zn += safeMultiply(aliment.getZn(), quantite / 100f);
        }

        return totals;
    }

    /**
     * Crée un aliment représentant le plat avec calcul des valeurs nutritionnelles
     * Version refactorisée utilisant calculateNutrientTotals()
     */
    private Aliment createAlimentForPlat(PlatInputDTO platDTO, Map<Aliment, Float> alimentsQuantites, User user) {
        float nbPortions = platDTO.nbPortions();
        NutrientTotals totals = calculateNutrientTotals(alimentsQuantites);

        // Création de l'aliment avec les valeurs par portion
        Aliment alimentPlat = Aliment.builder()
                .nom(platDTO.nom())
                .unite("portion")
                .calories(totals.calories / nbPortions)
                .matieresGrasses(safeDivide(totals.matieresGrasses, nbPortions))
                .matieresGrassesSatures(safeDivide(totals.matieresGrassesSatures, nbPortions))
                .matieresGrassesMonoInsaturees(safeDivide(totals.matieresGrassesMonoInsaturees, nbPortions))
                .matieresGrassesPolyInsaturees(safeDivide(totals.matieresGrassesPolyInsaturees, nbPortions))
                .matieresGrassesTrans(safeDivide(totals.matieresGrassesTrans, nbPortions))
                .proteines(safeDivide(totals.proteines, nbPortions))
                .glucides(safeDivide(totals.glucides, nbPortions))
                .sucre(safeDivide(totals.sucre, nbPortions))
                .fibres(safeDivide(totals.fibres, nbPortions))
                .sel(safeDivide(totals.sel, nbPortions))
                .cholesterol(safeDivide(totals.cholesterol, nbPortions))
                .provitamineA(safeDivide(totals.provitamineA, nbPortions))
                .vitamineA(safeDivide(totals.vitamineA, nbPortions))
                .vitamineB1(safeDivide(totals.vitamineB1, nbPortions))
                .vitamineB2(safeDivide(totals.vitamineB2, nbPortions))
                .vitamineB3(safeDivide(totals.vitamineB3, nbPortions))
                .vitamineB5(safeDivide(totals.vitamineB5, nbPortions))
                .vitamineB6(safeDivide(totals.vitamineB6, nbPortions))
                .vitamineB8(safeDivide(totals.vitamineB8, nbPortions))
                .vitamineB9(safeDivide(totals.vitamineB9, nbPortions))
                .vitamineB11(safeDivide(totals.vitamineB11, nbPortions))
                .vitamineB12(safeDivide(totals.vitamineB12, nbPortions))
                .vitamineC(safeDivide(totals.vitamineC, nbPortions))
                .vitamineD(safeDivide(totals.vitamineD, nbPortions))
                .vitamineE(safeDivide(totals.vitamineE, nbPortions))
                .vitamineK1(safeDivide(totals.vitamineK1, nbPortions))
                .vitamineK2(safeDivide(totals.vitamineK2, nbPortions))
                .Ars(safeDivide(totals.ars, nbPortions))
                .B(safeDivide(totals.b, nbPortions))
                .Ca(safeDivide(totals.ca, nbPortions))
                .Cl(safeDivide(totals.cl, nbPortions))
                .choline(safeDivide(totals.choline, nbPortions))
                .Cr(safeDivide(totals.cr, nbPortions))
                .Co(safeDivide(totals.co, nbPortions))
                .Cu(safeDivide(totals.cu, nbPortions))
                .Fe(safeDivide(totals.fe, nbPortions))
                .F(safeDivide(totals.f, nbPortions))
                .I(safeDivide(totals.i, nbPortions))
                .Mg(safeDivide(totals.mg, nbPortions))
                .Mn(safeDivide(totals.mn, nbPortions))
                .Mo(safeDivide(totals.mo, nbPortions))
                .Na(safeDivide(totals.na, nbPortions))
                .P(safeDivide(totals.p, nbPortions))
                .K(safeDivide(totals.k, nbPortions))
                .Rb(safeDivide(totals.rb, nbPortions))
                .SiO(safeDivide(totals.sio, nbPortions))
                .S(safeDivide(totals.s, nbPortions))
                .Se(safeDivide(totals.se, nbPortions))
                .V(safeDivide(totals.v, nbPortions))
                .Sn(safeDivide(totals.sn, nbPortions))
                .Zn(safeDivide(totals.zn, nbPortions))
                .user(user)
                .build();

        // Création du plat associé
        Plat plat = new Plat();
        plat.setNbPortions(nbPortions);
        plat.setAliment(alimentPlat);

        // Association bidirectionnelle
        alimentPlat.setPlat(plat);

        return alimentPlat;
    }

    /**
     * Compare deux maps de recettes pour voir si elles sont identiques
     */
    private boolean areRecettesIdentical(List<Recette> existantes, Map<Aliment, Float> nouvelles) {
        if (existantes.size() != nouvelles.size()) {
            return false;
        }

        for (Recette recette : existantes) {
            Aliment aliment = recette.getAliment();
            Float quantiteExistante = recette.getQuantite();
            Float quantiteNouvelle = nouvelles.get(aliment);

            if (quantiteNouvelle == null || !Objects.equals(quantiteExistante, quantiteNouvelle)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Met à jour seulement les données du plat (nom, nbPortions) sans toucher aux recettes
     */
    private void updatePlatDataOnly(Plat platExistant, PlatInputDTO platDTO,
                                    Map<Aliment, Float> alimentsQuantites, User user) {

        Aliment alimentExistant = platExistant.getAliment();
        alimentExistant.setNom(platDTO.nom());

        if (platExistant.getNbPortions() != platDTO.nbPortions()) {
            // Mise à jour du nombre de portions
            platExistant.setNbPortions(platDTO.nbPortions());

            // Recalcul des valeurs nutritionnelles avec le nouveau nombre de portions
            NutrientTotals totals = calculateNutrientTotals(alimentsQuantites);
            float nbPortions = platDTO.nbPortions();

            // Mise à jour des propriétés de l'aliment existant
            updateAlimentWithTotals(alimentExistant, totals, nbPortions);
        }

        // Sauvegarde
        alimentRepository.save(alimentExistant);
    }

    /**
     * Met à jour le plat avec de nouvelles recettes (suppression + recréation)
     */
    private void updatePlatWithNewRecettes(Plat platExistant, PlatInputDTO platDTO,
                                           Map<Aliment, Float> nouvellesRecettes, User user) {

        // Suppression des anciennes recettes
        List<Recette> anciennesRecettes = recetteRepository.findAllByRecetteId_PlatId(platExistant.getId());
        recetteRepository.deleteAll(anciennesRecettes);

        // Mise à jour du nombre de portions
        platExistant.setNbPortions(platDTO.nbPortions());

        // Recalcul des valeurs nutritionnelles
        NutrientTotals totals = calculateNutrientTotals(nouvellesRecettes);
        float nbPortions = platDTO.nbPortions();

        // Mise à jour des propriétés de l'aliment existant
        Aliment alimentExistant = platExistant.getAliment();
        alimentExistant.setNom(platDTO.nom());
        updateAlimentWithTotals(alimentExistant, totals, nbPortions);

        // Sauvegarde de l'aliment modifié
        alimentRepository.save(alimentExistant);

        // Création des nouvelles recettes
        saveRecettes(platExistant, nouvellesRecettes);
    }

    /**
     * Met à jour un aliment existant avec les nouveaux totaux nutritionnels
     */
    private void updateAlimentWithTotals(Aliment alimentExistant,
                                         NutrientTotals totals, float nbPortions) {
        alimentExistant.setCalories(totals.calories / nbPortions);
        alimentExistant.setMatieresGrasses(safeDivide(totals.matieresGrasses, nbPortions));
        alimentExistant.setMatieresGrassesSatures(safeDivide(totals.matieresGrassesSatures, nbPortions));
        alimentExistant.setMatieresGrassesMonoInsaturees(safeDivide(totals.matieresGrassesMonoInsaturees, nbPortions));
        alimentExistant.setMatieresGrassesPolyInsaturees(safeDivide(totals.matieresGrassesPolyInsaturees, nbPortions));
        alimentExistant.setMatieresGrassesTrans(safeDivide(totals.matieresGrassesTrans, nbPortions));
        alimentExistant.setProteines(safeDivide(totals.proteines, nbPortions));
        alimentExistant.setGlucides(safeDivide(totals.glucides, nbPortions));
        alimentExistant.setSucre(safeDivide(totals.sucre, nbPortions));
        alimentExistant.setFibres(safeDivide(totals.fibres, nbPortions));
        alimentExistant.setSel(safeDivide(totals.sel, nbPortions));
        alimentExistant.setCholesterol(safeDivide(totals.cholesterol, nbPortions));
        alimentExistant.setProvitamineA(safeDivide(totals.provitamineA, nbPortions));
        alimentExistant.setVitamineA(safeDivide(totals.vitamineA, nbPortions));
        alimentExistant.setVitamineB1(safeDivide(totals.vitamineB1, nbPortions));
        alimentExistant.setVitamineB2(safeDivide(totals.vitamineB2, nbPortions));
        alimentExistant.setVitamineB3(safeDivide(totals.vitamineB3, nbPortions));
        alimentExistant.setVitamineB5(safeDivide(totals.vitamineB5, nbPortions));
        alimentExistant.setVitamineB6(safeDivide(totals.vitamineB6, nbPortions));
        alimentExistant.setVitamineB8(safeDivide(totals.vitamineB8, nbPortions));
        alimentExistant.setVitamineB9(safeDivide(totals.vitamineB9, nbPortions));
        alimentExistant.setVitamineB11(safeDivide(totals.vitamineB11, nbPortions));
        alimentExistant.setVitamineB12(safeDivide(totals.vitamineB12, nbPortions));
        alimentExistant.setVitamineC(safeDivide(totals.vitamineC, nbPortions));
        alimentExistant.setVitamineD(safeDivide(totals.vitamineD, nbPortions));
        alimentExistant.setVitamineE(safeDivide(totals.vitamineE, nbPortions));
        alimentExistant.setVitamineK1(safeDivide(totals.vitamineK1, nbPortions));
        alimentExistant.setVitamineK2(safeDivide(totals.vitamineK2, nbPortions));
        alimentExistant.setArs(safeDivide(totals.ars, nbPortions));
        alimentExistant.setB(safeDivide(totals.b, nbPortions));
        alimentExistant.setCa(safeDivide(totals.ca, nbPortions));
        alimentExistant.setCl(safeDivide(totals.cl, nbPortions));
        alimentExistant.setCholine(safeDivide(totals.choline, nbPortions));
        alimentExistant.setCr(safeDivide(totals.cr, nbPortions));
        alimentExistant.setCo(safeDivide(totals.co, nbPortions));
        alimentExistant.setCu(safeDivide(totals.cu, nbPortions));
        alimentExistant.setFe(safeDivide(totals.fe, nbPortions));
        alimentExistant.setF(safeDivide(totals.f, nbPortions));
        alimentExistant.setI(safeDivide(totals.i, nbPortions));
        alimentExistant.setMg(safeDivide(totals.mg, nbPortions));
        alimentExistant.setMn(safeDivide(totals.mn, nbPortions));
        alimentExistant.setMo(safeDivide(totals.mo, nbPortions));
        alimentExistant.setNa(safeDivide(totals.na, nbPortions));
        alimentExistant.setP(safeDivide(totals.p, nbPortions));
        alimentExistant.setK(safeDivide(totals.k, nbPortions));
        alimentExistant.setRb(safeDivide(totals.rb, nbPortions));
        alimentExistant.setSiO(safeDivide(totals.sio, nbPortions));
        alimentExistant.setS(safeDivide(totals.s, nbPortions));
        alimentExistant.setSe(safeDivide(totals.se, nbPortions));
        alimentExistant.setV(safeDivide(totals.v, nbPortions));
        alimentExistant.setSn(safeDivide(totals.sn, nbPortions));
        alimentExistant.setZn(safeDivide(totals.zn, nbPortions));
    }

    /**
     * Vérifie que le plat existe et appartient à l'utilisateur spécifié
     * @param platId ID du plat à vérifier
     * @param userId ID de l'utilisateur
     * @return le plat si les vérifications passent
     * @throws NotFoundException si le plat n'existe pas
     * @throws ForbiddenException si le plat n'appartient pas à l'utilisateur
     */
    private Plat findPlatAndCheckOwnership(long platId, long userId) {
        // 1. Vérifier que le plat existe
        Plat plat = platRepository.findById(platId)
                .orElseThrow(() -> new NotFoundException("Plat non trouvé"));

        // 2. Vérifier que l'utilisateur a le droit d'y accéder
        if (!plat.getAliment().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Vous n'êtes pas autorisé à accéder à ce plat");
        }

        return plat;
    }

    /**
     * Multiplie deux valeurs Float en gérant les valeurs null
     */
    private float safeMultiply(Float value, float multiplier) {
        return value != null ? value * multiplier : 0f;
    }

    /**
     * Divise une valeur par un diviseur en gérant les valeurs null et la division par zéro
     */
    private Float safeDivide(float value, float divisor) {
        if (divisor == 0f || value == 0f) {
            return null;
        }
        return value / divisor;
    }

    /**
     * Classe interne pour regrouper les totaux nutritionnels
     */
    private static class NutrientTotals {
        float calories = 0f;
        float matieresGrasses = 0f;
        float matieresGrassesSatures = 0f;
        float matieresGrassesMonoInsaturees = 0f;
        float matieresGrassesPolyInsaturees = 0f;
        float matieresGrassesTrans = 0f;
        float proteines = 0f;
        float glucides = 0f;
        float sucre = 0f;
        float fibres = 0f;
        float sel = 0f;
        float cholesterol = 0f;
        float provitamineA = 0f;
        float vitamineA = 0f;
        float vitamineB1 = 0f;
        float vitamineB2 = 0f;
        float vitamineB3 = 0f;
        float vitamineB5 = 0f;
        float vitamineB6 = 0f;
        float vitamineB8 = 0f;
        float vitamineB9 = 0f;
        float vitamineB11 = 0f;
        float vitamineB12 = 0f;
        float vitamineC = 0f;
        float vitamineD = 0f;
        float vitamineE = 0f;
        float vitamineK1 = 0f;
        float vitamineK2 = 0f;
        float ars = 0f;
        float b = 0f;
        float ca = 0f;
        float cl = 0f;
        float choline = 0f;
        float cr = 0f;
        float co = 0f;
        float cu = 0f;
        float fe = 0f;
        float f = 0f;
        float i = 0f;
        float mg = 0f;
        float mn = 0f;
        float mo = 0f;
        float na = 0f;
        float p = 0f;
        float k = 0f;
        float rb = 0f;
        float sio = 0f;
        float s = 0f;
        float se = 0f;
        float v = 0f;
        float sn = 0f;
        float zn = 0f;
    }
}