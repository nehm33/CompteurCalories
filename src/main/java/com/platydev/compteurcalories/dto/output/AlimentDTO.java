package com.platydev.compteurcalories.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlimentDTO(Long id, String nom, Float calories, String unite, Float matieresGrasses,
                         Float matieresGrassesSatures, Float matieresGrassesMonoInsaturees, Float matieresGrassesPolyInsaturees,
                         Float matieresGrassesTrans, Float proteines, Float glucides, Float sucre, Float fibres, Float sel,
                         Float cholesterol, Float provitamineA, Float vitamineA, Float vitamineB1, Float vitamineB2,
                         Float vitamineB3, Float vitamineB5, Float vitamineB6, Float vitamineB8, Float vitamineB9,
                         Float vitamineB11, Float vitamineB12, Float vitamineC, Float vitamineD, Float vitamineE,
                         Float vitamineK1, Float vitamineK2, Float Ars, Float B, Float Ca, Float Cl, Float choline, Float Cr,
                         Float Co, Float Cu, Float Fe, Float F, Float I, Float Mg, Float Mn, Float Mo, Float Na, Float P,
                         Float K, Float Rb, Float SiO, Float S, Float Se, Float V, Float Sn, Float Zn,
                         String codeBarre) {
}
