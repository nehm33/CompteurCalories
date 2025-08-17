package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.output.NutrientTotals;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NutritionalCalculator {

    public NutrientTotals calculateTotals(Map<Aliment, Float> alimentsQuantites) {
        NutrientTotals totals = new NutrientTotals();

        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            Aliment aliment = entry.getKey();
            float quantite = entry.getValue();
            float multiplier = "portion".equals(aliment.getUnite()) ? quantite : quantite / 100f;

            totals.setCalories(totals.getCalories() + safeMultiply(aliment.getCalories(), multiplier));
            totals.setMatieresGrasses(totals.getMatieresGrasses() + safeMultiply(aliment.getMatieresGrasses(), multiplier));
            totals.setMatieresGrassesSatures(totals.getMatieresGrassesSatures() + safeMultiply(aliment.getMatieresGrassesSatures(), multiplier));
            totals.setMatieresGrassesMonoInsaturees(totals.getMatieresGrassesMonoInsaturees() + safeMultiply(aliment.getMatieresGrassesMonoInsaturees(), multiplier));
            totals.setMatieresGrassesPolyInsaturees(totals.getMatieresGrassesPolyInsaturees() + safeMultiply(aliment.getMatieresGrassesPolyInsaturees(), multiplier));
            totals.setMatieresGrassesTrans(totals.getMatieresGrassesTrans() + safeMultiply(aliment.getMatieresGrassesTrans(), multiplier));
            totals.setProteines(totals.getProteines() + safeMultiply(aliment.getProteines(), multiplier));
            totals.setGlucides(totals.getGlucides() + safeMultiply(aliment.getGlucides(), multiplier));
            totals.setSucre(totals.getSucre() + safeMultiply(aliment.getSucre(), multiplier));
            totals.setFibres(totals.getFibres() + safeMultiply(aliment.getFibres(), multiplier));
            totals.setSel(totals.getSel() + safeMultiply(aliment.getSel(), multiplier));
            totals.setCholesterol(totals.getCholesterol() + safeMultiply(aliment.getCholesterol(), multiplier));
            totals.setProvitamineA(totals.getProvitamineA() + safeMultiply(aliment.getProvitamineA(), multiplier));
            totals.setVitamineA(totals.getVitamineA() + safeMultiply(aliment.getVitamineA(), multiplier));
            totals.setVitamineB1(totals.getVitamineB1() + safeMultiply(aliment.getVitamineB1(), multiplier));
            totals.setVitamineB2(totals.getVitamineB2() + safeMultiply(aliment.getVitamineB2(), multiplier));
            totals.setVitamineB3(totals.getVitamineB3() + safeMultiply(aliment.getVitamineB3(), multiplier));
            totals.setVitamineB5(totals.getVitamineB5() + safeMultiply(aliment.getVitamineB5(), multiplier));
            totals.setVitamineB6(totals.getVitamineB6() + safeMultiply(aliment.getVitamineB6(), multiplier));
            totals.setVitamineB8(totals.getVitamineB8() + safeMultiply(aliment.getVitamineB8(), multiplier));
            totals.setVitamineB9(totals.getVitamineB9() + safeMultiply(aliment.getVitamineB9(), multiplier));
            totals.setVitamineB11(totals.getVitamineB11() + safeMultiply(aliment.getVitamineB11(), multiplier));
            totals.setVitamineB12(totals.getVitamineB12() + safeMultiply(aliment.getVitamineB12(), multiplier));
            totals.setVitamineC(totals.getVitamineC() + safeMultiply(aliment.getVitamineC(), multiplier));
            totals.setVitamineD(totals.getVitamineD() + safeMultiply(aliment.getVitamineD(), multiplier));
            totals.setVitamineE(totals.getVitamineE() + safeMultiply(aliment.getVitamineE(), multiplier));
            totals.setVitamineK1(totals.getVitamineK1() + safeMultiply(aliment.getVitamineK1(), multiplier));
            totals.setVitamineK2(totals.getVitamineK2() + safeMultiply(aliment.getVitamineK2(), multiplier));
            totals.setArs(totals.getArs() + safeMultiply(aliment.getArs(), multiplier));
            totals.setB(totals.getB() + safeMultiply(aliment.getB(), multiplier));
            totals.setCa(totals.getCa() + safeMultiply(aliment.getCa(), multiplier));
            totals.setCl(totals.getCl() + safeMultiply(aliment.getCl(), multiplier));
            totals.setCholine(totals.getCholine() + safeMultiply(aliment.getCholine(), multiplier));
            totals.setCr(totals.getCr() + safeMultiply(aliment.getCr(), multiplier));
            totals.setCo(totals.getCo() + safeMultiply(aliment.getCo(), multiplier));
            totals.setCu(totals.getCu() + safeMultiply(aliment.getCu(), multiplier));
            totals.setFe(totals.getFe() + safeMultiply(aliment.getFe(), multiplier));
            totals.setF(totals.getF() + safeMultiply(aliment.getF(), multiplier));
            totals.setI(totals.getI() + safeMultiply(aliment.getI(), multiplier));
            totals.setMg(totals.getMg() + safeMultiply(aliment.getMg(), multiplier));
            totals.setMn(totals.getMn() + safeMultiply(aliment.getMn(), multiplier));
            totals.setMo(totals.getMo() + safeMultiply(aliment.getMo(), multiplier));
            totals.setNa(totals.getNa() + safeMultiply(aliment.getNa(), multiplier));
            totals.setP(totals.getP() + safeMultiply(aliment.getP(), multiplier));
            totals.setK(totals.getK() + safeMultiply(aliment.getK(), multiplier));
            totals.setRb(totals.getRb() + safeMultiply(aliment.getRb(), multiplier));
            totals.setSio(totals.getSio() + safeMultiply(aliment.getSiO(), multiplier));
            totals.setS(totals.getS() + safeMultiply(aliment.getS(), multiplier));
            totals.setSe(totals.getSe() + safeMultiply(aliment.getSe(), multiplier));
            totals.setV(totals.getV() + safeMultiply(aliment.getV(), multiplier));
            totals.setSn(totals.getSn() + safeMultiply(aliment.getSn(), multiplier));
            totals.setZn(totals.getZn() + safeMultiply(aliment.getZn(), multiplier));
        }

        return totals;
    }

    /**
     * Calcule le total des calories pour une Map d'aliments et quantités
     */
    public float calculateTotalCalories(Map<Aliment, Float> alimentsQuantites) {
        float total = 0f;
        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            Aliment aliment = entry.getKey();
            float quantite = entry.getValue();

            if (aliment.getCalories() != null) {
                float multiplier = "portion".equals(aliment.getUnite()) ? quantite : quantite / 100f;
                total += aliment.getCalories() * multiplier;
            }
        }
        return total;
    }

    public Aliment createPlatAliment(PlatInputDTO platDTO, NutrientTotals totals, User user) {
        float nbPortions = platDTO.nbPortions();

        return Aliment.builder()
                .nom(platDTO.nom())
                .unite("portion")
                .calories(totals.getCalories() / nbPortions)
                .matieresGrasses(safeDivide(totals.getMatieresGrasses(), nbPortions))
                .matieresGrassesSatures(safeDivide(totals.getMatieresGrassesSatures(), nbPortions))
                .matieresGrassesMonoInsaturees(safeDivide(totals.getMatieresGrassesMonoInsaturees(), nbPortions))
                .matieresGrassesPolyInsaturees(safeDivide(totals.getMatieresGrassesPolyInsaturees(), nbPortions))
                .matieresGrassesTrans(safeDivide(totals.getMatieresGrassesTrans(), nbPortions))
                .proteines(safeDivide(totals.getProteines(), nbPortions))
                .glucides(safeDivide(totals.getGlucides(), nbPortions))
                .sucre(safeDivide(totals.getSucre(), nbPortions))
                .fibres(safeDivide(totals.getFibres(), nbPortions))
                .sel(safeDivide(totals.getSel(), nbPortions))
                .cholesterol(safeDivide(totals.getCholesterol(), nbPortions))
                .provitamineA(safeDivide(totals.getProvitamineA(), nbPortions))
                .vitamineA(safeDivide(totals.getVitamineA(), nbPortions))
                .vitamineB1(safeDivide(totals.getVitamineB1(), nbPortions))
                .vitamineB2(safeDivide(totals.getVitamineB2(), nbPortions))
                .vitamineB3(safeDivide(totals.getVitamineB3(), nbPortions))
                .vitamineB5(safeDivide(totals.getVitamineB5(), nbPortions))
                .vitamineB6(safeDivide(totals.getVitamineB6(), nbPortions))
                .vitamineB8(safeDivide(totals.getVitamineB8(), nbPortions))
                .vitamineB9(safeDivide(totals.getVitamineB9(), nbPortions))
                .vitamineB11(safeDivide(totals.getVitamineB11(), nbPortions))
                .vitamineB12(safeDivide(totals.getVitamineB12(), nbPortions))
                .vitamineC(safeDivide(totals.getVitamineC(), nbPortions))
                .vitamineD(safeDivide(totals.getVitamineD(), nbPortions))
                .vitamineE(safeDivide(totals.getVitamineE(), nbPortions))
                .vitamineK1(safeDivide(totals.getVitamineK1(), nbPortions))
                .vitamineK2(safeDivide(totals.getVitamineK2(), nbPortions))
                .Ars(safeDivide(totals.getArs(), nbPortions))
                .B(safeDivide(totals.getB(), nbPortions))
                .Ca(safeDivide(totals.getCa(), nbPortions))
                .Cl(safeDivide(totals.getCl(), nbPortions))
                .choline(safeDivide(totals.getCholine(), nbPortions))
                .Cr(safeDivide(totals.getCr(), nbPortions))
                .Co(safeDivide(totals.getCo(), nbPortions))
                .Cu(safeDivide(totals.getCu(), nbPortions))
                .Fe(safeDivide(totals.getFe(), nbPortions))
                .F(safeDivide(totals.getF(), nbPortions))
                .I(safeDivide(totals.getI(), nbPortions))
                .Mg(safeDivide(totals.getMg(), nbPortions))
                .Mn(safeDivide(totals.getMn(), nbPortions))
                .Mo(safeDivide(totals.getMo(), nbPortions))
                .Na(safeDivide(totals.getNa(), nbPortions))
                .P(safeDivide(totals.getP(), nbPortions))
                .K(safeDivide(totals.getK(), nbPortions))
                .Rb(safeDivide(totals.getRb(), nbPortions))
                .SiO(safeDivide(totals.getSio(), nbPortions))
                .S(safeDivide(totals.getS(), nbPortions))
                .Se(safeDivide(totals.getSe(), nbPortions))
                .V(safeDivide(totals.getV(), nbPortions))
                .Sn(safeDivide(totals.getSn(), nbPortions))
                .Zn(safeDivide(totals.getZn(), nbPortions))
                .user(user)
                .build();
    }

    public void updateAlimentWithTotals(Aliment aliment, NutrientTotals totals, float nbPortions) {
        aliment.setCalories(totals.getCalories() / nbPortions);
        aliment.setMatieresGrasses(safeDivide(totals.getMatieresGrasses(), nbPortions));
        aliment.setMatieresGrassesSatures(safeDivide(totals.getMatieresGrassesSatures(), nbPortions));
        aliment.setMatieresGrassesMonoInsaturees(safeDivide(totals.getMatieresGrassesMonoInsaturees(), nbPortions));
        aliment.setMatieresGrassesPolyInsaturees(safeDivide(totals.getMatieresGrassesPolyInsaturees(), nbPortions));
        aliment.setMatieresGrassesTrans(safeDivide(totals.getMatieresGrassesTrans(), nbPortions));
        aliment.setProteines(safeDivide(totals.getProteines(), nbPortions));
        aliment.setGlucides(safeDivide(totals.getGlucides(), nbPortions));
        aliment.setSucre(safeDivide(totals.getSucre(), nbPortions));
        aliment.setFibres(safeDivide(totals.getFibres(), nbPortions));
        aliment.setSel(safeDivide(totals.getSel(), nbPortions));
        aliment.setCholesterol(safeDivide(totals.getCholesterol(), nbPortions));
        aliment.setProvitamineA(safeDivide(totals.getProvitamineA(), nbPortions));
        aliment.setVitamineA(safeDivide(totals.getVitamineA(), nbPortions));
        aliment.setVitamineB1(safeDivide(totals.getVitamineB1(), nbPortions));
        aliment.setVitamineB2(safeDivide(totals.getVitamineB2(), nbPortions));
        aliment.setVitamineB3(safeDivide(totals.getVitamineB3(), nbPortions));
        aliment.setVitamineB5(safeDivide(totals.getVitamineB5(), nbPortions));
        aliment.setVitamineB6(safeDivide(totals.getVitamineB6(), nbPortions));
        aliment.setVitamineB8(safeDivide(totals.getVitamineB8(), nbPortions));
        aliment.setVitamineB9(safeDivide(totals.getVitamineB9(), nbPortions));
        aliment.setVitamineB11(safeDivide(totals.getVitamineB11(), nbPortions));
        aliment.setVitamineB12(safeDivide(totals.getVitamineB12(), nbPortions));
        aliment.setVitamineC(safeDivide(totals.getVitamineC(), nbPortions));
        aliment.setVitamineD(safeDivide(totals.getVitamineD(), nbPortions));
        aliment.setVitamineE(safeDivide(totals.getVitamineE(), nbPortions));
        aliment.setVitamineK1(safeDivide(totals.getVitamineK1(), nbPortions));
        aliment.setVitamineK2(safeDivide(totals.getVitamineK2(), nbPortions));
        aliment.setArs(safeDivide(totals.getArs(), nbPortions));
        aliment.setB(safeDivide(totals.getB(), nbPortions));
        aliment.setCa(safeDivide(totals.getCa(), nbPortions));
        aliment.setCl(safeDivide(totals.getCl(), nbPortions));
        aliment.setCholine(safeDivide(totals.getCholine(), nbPortions));
        aliment.setCr(safeDivide(totals.getCr(), nbPortions));
        aliment.setCo(safeDivide(totals.getCo(), nbPortions));
        aliment.setCu(safeDivide(totals.getCu(), nbPortions));
        aliment.setFe(safeDivide(totals.getFe(), nbPortions));
        aliment.setF(safeDivide(totals.getF(), nbPortions));
        aliment.setI(safeDivide(totals.getI(), nbPortions));
        aliment.setMg(safeDivide(totals.getMg(), nbPortions));
        aliment.setMn(safeDivide(totals.getMn(), nbPortions));
        aliment.setMo(safeDivide(totals.getMo(), nbPortions));
        aliment.setNa(safeDivide(totals.getNa(), nbPortions));
        aliment.setP(safeDivide(totals.getP(), nbPortions));
        aliment.setK(safeDivide(totals.getK(), nbPortions));
        aliment.setRb(safeDivide(totals.getRb(), nbPortions));
        aliment.setSiO(safeDivide(totals.getSio(), nbPortions));
        aliment.setS(safeDivide(totals.getS(), nbPortions));
        aliment.setSe(safeDivide(totals.getSe(), nbPortions));
        aliment.setV(safeDivide(totals.getV(), nbPortions));
        aliment.setSn(safeDivide(totals.getSn(), nbPortions));
        aliment.setZn(safeDivide(totals.getZn(), nbPortions));
    }

    private float safeMultiply(Float value, float multiplier) {
        return value != null ? value * multiplier : 0f;
    }

    private Float safeDivide(float value, float divisor) {
        if (divisor == 0f || value == 0f) {
            return null;
        }
        return value / divisor;
    }
}