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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public void add(PlatInputDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Validation des données d'entrée
        if (platDTO.recettes() == null || platDTO.recettes().isEmpty()) {
            throw new ApiException("Un plat doit contenir au moins une recette");
        }

        if (platDTO.nbPortions() == null || platDTO.nbPortions() <= 0) {
            throw new ApiException("Le nombre de portions doit être supérieur à 0");
        }

        if (platDTO.nom() == null || platDTO.nom().trim().isEmpty()) {
            throw new ApiException("Le nom du plat est obligatoire");
        }

        // Vérification et récupération des aliments des recettes
        List<Aliment> alimentsRecettes = new ArrayList<>();
        for (RecetteInputDTO recetteDTO : platDTO.recettes()) {
            if (recetteDTO.alimentId() == null) {
                throw new ApiException("L'ID de l'aliment est obligatoire pour chaque recette");
            }

            if (recetteDTO.quantite() == null || recetteDTO.quantite() <= 0) {
                throw new ApiException("La quantité doit être supérieure à 0 pour chaque recette");
            }

            Aliment aliment = alimentRepository.findById(recetteDTO.alimentId())
                    .orElseThrow(() -> new NotFoundException("Aliment avec l'ID " + recetteDTO.alimentId() + " non trouvé"));

            // Vérifier que l'utilisateur a accès à cet aliment
            if (!aliment.getUser().equals(currentUser) && aliment.getUser().getId() != 1L) {
                throw new ForbiddenException("Vous n'avez pas accès à l'aliment avec l'ID " + recetteDTO.alimentId());
            }

            alimentsRecettes.add(aliment);
        }

        // Création de l'aliment représentant le plat avec calcul des valeurs nutritionnelles
        Aliment alimentPlat = createAlimentForPlat(platDTO, alimentsRecettes, currentUser);

        // Sauvegarde de l'aliment (cascade vers le plat)
        Aliment savedAliment = alimentRepository.save(alimentPlat);

        // Création et sauvegarde des recettes
        List<Recette> recettes = new ArrayList<>();
        for (int i = 0; i < platDTO.recettes().size(); i++) {
            RecetteInputDTO recetteDTO = platDTO.recettes().get(i);
            Aliment alimentRecette = alimentsRecettes.get(i);

            Recette recette = new Recette(savedAliment.getPlat(), alimentRecette, recetteDTO.quantite());
            recettes.add(recette);
        }

        recetteRepository.saveAll(recettes);
    }

    @Override
    public void update(long platId, PlatInputDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());

        // Implémentation de la mise à jour à venir
    }

    @Override
    public void delete(long platId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());

        alimentRepository.deleteById(plat.getAliment().getId());
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
     * Crée un aliment représentant le plat avec calcul des valeurs nutritionnelles
     */
    private Aliment createAlimentForPlat(PlatInputDTO platDTO, List<Aliment> alimentsRecettes, User user) {
        float nbPortions = platDTO.nbPortions();

        // Variables pour les calculs
        float totalCalories = 0f;
        float totalMatieresGrasses = 0f;
        float totalMatieresGrassesSatures = 0f;
        float totalMatieresGrassesMonoInsaturees = 0f;
        float totalMatieresGrassesPolyInsaturees = 0f;
        float totalMatieresGrassesTrans = 0f;
        float totalProteines = 0f;
        float totalGlucides = 0f;
        float totalSucre = 0f;
        float totalFibres = 0f;
        float totalSel = 0f;
        float totalCholesterol = 0f;
        float totalProvitamineA = 0f;
        float totalVitamineA = 0f;
        float totalVitamineB1 = 0f;
        float totalVitamineB2 = 0f;
        float totalVitamineB3 = 0f;
        float totalVitamineB5 = 0f;
        float totalVitamineB6 = 0f;
        float totalVitamineB8 = 0f;
        float totalVitamineB9 = 0f;
        float totalVitamineB11 = 0f;
        float totalVitamineB12 = 0f;
        float totalVitamineC = 0f;
        float totalVitamineD = 0f;
        float totalVitamineE = 0f;
        float totalVitamineK1 = 0f;
        float totalVitamineK2 = 0f;
        float totalArs = 0f;
        float totalB = 0f;
        float totalCa = 0f;
        float totalCl = 0f;
        float totalCholine = 0f;
        float totalCr = 0f;
        float totalCo = 0f;
        float totalCu = 0f;
        float totalFe = 0f;
        float totalF = 0f;
        float totalI = 0f;
        float totalMg = 0f;
        float totalMn = 0f;
        float totalMo = 0f;
        float totalNa = 0f;
        float totalP = 0f;
        float totalK = 0f;
        float totalRb = 0f;
        float totalSiO = 0f;
        float totalS = 0f;
        float totalSe = 0f;
        float totalV = 0f;
        float totalSn = 0f;
        float totalZn = 0f;

        // Calcul des totaux pour chaque nutriment
        for (int i = 0; i < platDTO.recettes().size(); i++) {
            RecetteInputDTO recetteDTO = platDTO.recettes().get(i);
            Aliment aliment = alimentsRecettes.get(i);
            float quantite = recetteDTO.quantite();

            totalCalories += safeMultiply(aliment.getCalories(), quantite / 100f);
            totalMatieresGrasses += safeMultiply(aliment.getMatieresGrasses(), quantite / 100f);
            totalMatieresGrassesSatures += safeMultiply(aliment.getMatieresGrassesSatures(), quantite / 100f);
            totalMatieresGrassesMonoInsaturees += safeMultiply(aliment.getMatieresGrassesMonoInsaturees(), quantite / 100f);
            totalMatieresGrassesPolyInsaturees += safeMultiply(aliment.getMatieresGrassesPolyInsaturees(), quantite / 100f);
            totalMatieresGrassesTrans += safeMultiply(aliment.getMatieresGrassesTrans(), quantite / 100f);
            totalProteines += safeMultiply(aliment.getProteines(), quantite / 100f);
            totalGlucides += safeMultiply(aliment.getGlucides(), quantite / 100f);
            totalSucre += safeMultiply(aliment.getSucre(), quantite / 100f);
            totalFibres += safeMultiply(aliment.getFibres(), quantite / 100f);
            totalSel += safeMultiply(aliment.getSel(), quantite / 100f);
            totalCholesterol += safeMultiply(aliment.getCholesterol(), quantite / 100f);
            totalProvitamineA += safeMultiply(aliment.getProvitamineA(), quantite / 100f);
            totalVitamineA += safeMultiply(aliment.getVitamineA(), quantite / 100f);
            totalVitamineB1 += safeMultiply(aliment.getVitamineB1(), quantite / 100f);
            totalVitamineB2 += safeMultiply(aliment.getVitamineB2(), quantite / 100f);
            totalVitamineB3 += safeMultiply(aliment.getVitamineB3(), quantite / 100f);
            totalVitamineB5 += safeMultiply(aliment.getVitamineB5(), quantite / 100f);
            totalVitamineB6 += safeMultiply(aliment.getVitamineB6(), quantite / 100f);
            totalVitamineB8 += safeMultiply(aliment.getVitamineB8(), quantite / 100f);
            totalVitamineB9 += safeMultiply(aliment.getVitamineB9(), quantite / 100f);
            totalVitamineB11 += safeMultiply(aliment.getVitamineB11(), quantite / 100f);
            totalVitamineB12 += safeMultiply(aliment.getVitamineB12(), quantite / 100f);
            totalVitamineC += safeMultiply(aliment.getVitamineC(), quantite / 100f);
            totalVitamineD += safeMultiply(aliment.getVitamineD(), quantite / 100f);
            totalVitamineE += safeMultiply(aliment.getVitamineE(), quantite / 100f);
            totalVitamineK1 += safeMultiply(aliment.getVitamineK1(), quantite / 100f);
            totalVitamineK2 += safeMultiply(aliment.getVitamineK2(), quantite / 100f);
            totalArs += safeMultiply(aliment.getArs(), quantite / 100f);
            totalB += safeMultiply(aliment.getB(), quantite / 100f);
            totalCa += safeMultiply(aliment.getCa(), quantite / 100f);
            totalCl += safeMultiply(aliment.getCl(), quantite / 100f);
            totalCholine += safeMultiply(aliment.getCholine(), quantite / 100f);
            totalCr += safeMultiply(aliment.getCr(), quantite / 100f);
            totalCo += safeMultiply(aliment.getCo(), quantite / 100f);
            totalCu += safeMultiply(aliment.getCu(), quantite / 100f);
            totalFe += safeMultiply(aliment.getFe(), quantite / 100f);
            totalF += safeMultiply(aliment.getF(), quantite / 100f);
            totalI += safeMultiply(aliment.getI(), quantite / 100f);
            totalMg += safeMultiply(aliment.getMg(), quantite / 100f);
            totalMn += safeMultiply(aliment.getMn(), quantite / 100f);
            totalMo += safeMultiply(aliment.getMo(), quantite / 100f);
            totalNa += safeMultiply(aliment.getNa(), quantite / 100f);
            totalP += safeMultiply(aliment.getP(), quantite / 100f);
            totalK += safeMultiply(aliment.getK(), quantite / 100f);
            totalRb += safeMultiply(aliment.getRb(), quantite / 100f);
            totalSiO += safeMultiply(aliment.getSiO(), quantite / 100f);
            totalS += safeMultiply(aliment.getS(), quantite / 100f);
            totalSe += safeMultiply(aliment.getSe(), quantite / 100f);
            totalV += safeMultiply(aliment.getV(), quantite / 100f);
            totalSn += safeMultiply(aliment.getSn(), quantite / 100f);
            totalZn += safeMultiply(aliment.getZn(), quantite / 100f);
        }

        // Création de l'aliment avec les valeurs par portion
        Aliment alimentPlat = Aliment.builder()
                .nom(platDTO.nom())
                .unite("portion")
                .calories(totalCalories / nbPortions)
                .matieresGrasses(safeDivide(totalMatieresGrasses, nbPortions))
                .matieresGrassesSatures(safeDivide(totalMatieresGrassesSatures, nbPortions))
                .matieresGrassesMonoInsaturees(safeDivide(totalMatieresGrassesMonoInsaturees, nbPortions))
                .matieresGrassesPolyInsaturees(safeDivide(totalMatieresGrassesPolyInsaturees, nbPortions))
                .matieresGrassesTrans(safeDivide(totalMatieresGrassesTrans, nbPortions))
                .proteines(safeDivide(totalProteines, nbPortions))
                .glucides(safeDivide(totalGlucides, nbPortions))
                .sucre(safeDivide(totalSucre, nbPortions))
                .fibres(safeDivide(totalFibres, nbPortions))
                .sel(safeDivide(totalSel, nbPortions))
                .cholesterol(safeDivide(totalCholesterol, nbPortions))
                .provitamineA(safeDivide(totalProvitamineA, nbPortions))
                .vitamineA(safeDivide(totalVitamineA, nbPortions))
                .vitamineB1(safeDivide(totalVitamineB1, nbPortions))
                .vitamineB2(safeDivide(totalVitamineB2, nbPortions))
                .vitamineB3(safeDivide(totalVitamineB3, nbPortions))
                .vitamineB5(safeDivide(totalVitamineB5, nbPortions))
                .vitamineB6(safeDivide(totalVitamineB6, nbPortions))
                .vitamineB8(safeDivide(totalVitamineB8, nbPortions))
                .vitamineB9(safeDivide(totalVitamineB9, nbPortions))
                .vitamineB11(safeDivide(totalVitamineB11, nbPortions))
                .vitamineB12(safeDivide(totalVitamineB12, nbPortions))
                .vitamineC(safeDivide(totalVitamineC, nbPortions))
                .vitamineD(safeDivide(totalVitamineD, nbPortions))
                .vitamineE(safeDivide(totalVitamineE, nbPortions))
                .vitamineK1(safeDivide(totalVitamineK1, nbPortions))
                .vitamineK2(safeDivide(totalVitamineK2, nbPortions))
                .Ars(safeDivide(totalArs, nbPortions))
                .B(safeDivide(totalB, nbPortions))
                .Ca(safeDivide(totalCa, nbPortions))
                .Cl(safeDivide(totalCl, nbPortions))
                .choline(safeDivide(totalCholine, nbPortions))
                .Cr(safeDivide(totalCr, nbPortions))
                .Co(safeDivide(totalCo, nbPortions))
                .Cu(safeDivide(totalCu, nbPortions))
                .Fe(safeDivide(totalFe, nbPortions))
                .F(safeDivide(totalF, nbPortions))
                .I(safeDivide(totalI, nbPortions))
                .Mg(safeDivide(totalMg, nbPortions))
                .Mn(safeDivide(totalMn, nbPortions))
                .Mo(safeDivide(totalMo, nbPortions))
                .Na(safeDivide(totalNa, nbPortions))
                .P(safeDivide(totalP, nbPortions))
                .K(safeDivide(totalK, nbPortions))
                .Rb(safeDivide(totalRb, nbPortions))
                .SiO(safeDivide(totalSiO, nbPortions))
                .S(safeDivide(totalS, nbPortions))
                .Se(safeDivide(totalSe, nbPortions))
                .V(safeDivide(totalV, nbPortions))
                .Sn(safeDivide(totalSn, nbPortions))
                .Zn(safeDivide(totalZn, nbPortions))
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
}