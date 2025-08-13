package com.platydev.compteurcalories.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record AlimentInputDTO(
        @NotBlank(message = "Le nom de l'aliment est obligatoire")
        @Size(max = 255, message = "Le nom de l'aliment ne peut pas dépasser 255 caractères")
        String nom,

        @PositiveOrZero(message = "Les calories ne peuvent pas être négatives")
        Float calories,

        @NotBlank(message = "L'unité de l'aliment est obligatoire")
        String unite,

        @PositiveOrZero(message = "Les matières grasses ne peuvent pas être négatives")
        Float matieresGrasses,

        @PositiveOrZero(message = "Les matières grasses saturées ne peuvent pas être négatives")
        Float matieresGrassesSatures,

        @PositiveOrZero(message = "Les matières grasses monoinsaturées ne peuvent pas être négatives")
        Float matieresGrassesMonoInsaturees,

        @PositiveOrZero(message = "Les matières grasses polyinsaturées ne peuvent pas être négatives")
        Float matieresGrassesPolyInsaturees,

        @PositiveOrZero(message = "Les matières grasses trans ne peuvent pas être négatives")
        Float matieresGrassesTrans,

        @PositiveOrZero(message = "Les protéines ne peuvent pas être négatives")
        Float proteines,

        @PositiveOrZero(message = "Les glucides ne peuvent pas être négatives")
        Float glucides,

        @PositiveOrZero(message = "Le sucre ne peut pas être négatif")
        Float sucre,

        @PositiveOrZero(message = "Les fibres ne peuvent pas être négatives")
        Float fibres,

        @PositiveOrZero(message = "Le sel ne peut pas être négatif")
        Float sel,

        @PositiveOrZero(message = "Le cholestérol ne peut pas être négatif")
        Float cholesterol,

        @PositiveOrZero(message = "La provitamine A ne peut pas être négative")
        Float provitamineA,

        @PositiveOrZero(message = "La vitamine A ne peut pas être négative")
        Float vitamineA,

        @PositiveOrZero(message = "La vitamine B1 ne peut pas être négative")
        Float vitamineB1,

        @PositiveOrZero(message = "La vitamine B2 ne peut pas être négative")
        Float vitamineB2,

        @PositiveOrZero(message = "La vitamine B3 ne peut pas être négative")
        Float vitamineB3,

        @PositiveOrZero(message = "La vitamine B5 ne peut pas être négative")
        Float vitamineB5,

        @PositiveOrZero(message = "La vitamine B6 ne peut pas être négative")
        Float vitamineB6,

        @PositiveOrZero(message = "La vitamine B8 ne peut pas être négative")
        Float vitamineB8,

        @PositiveOrZero(message = "La vitamine B9 ne peut pas être négative")
        Float vitamineB9,

        @PositiveOrZero(message = "La vitamine B11 ne peut pas être négative")
        Float vitamineB11,

        @PositiveOrZero(message = "La vitamine B12 ne peut peut pas être négative")
        Float vitamineB12,

        @PositiveOrZero(message = "La vitamine C ne peut pas être négative")
        Float vitamineC,

        @PositiveOrZero(message = "La vitamine D ne peut pas être négative")
        Float vitamineD,

        @PositiveOrZero(message = "La vitamine E ne peut pas être négative")
        Float vitamineE,

        @PositiveOrZero(message = "La vitamine K1 ne peut pas être négative")
        Float vitamineK1,

        @PositiveOrZero(message = "La vitamine K2 ne peut pas être négative")
        Float vitamineK2,

        // Minéraux et oligo-éléments
        @PositiveOrZero(message = "L'arsenic ne peut pas être négatif")
        Float Ars,

        @PositiveOrZero(message = "Le bore ne peut pas être négatif")
        Float B,

        @PositiveOrZero(message = "Le calcium ne peut pas être négatif")
        Float Ca,

        @PositiveOrZero(message = "Le chlore ne peut pas être négatif")
        Float Cl,

        @PositiveOrZero(message = "La choline ne peut pas être négative")
        Float choline,

        @PositiveOrZero(message = "Le chrome ne peut pas être négatif")
        Float Cr,

        @PositiveOrZero(message = "Le cobalt ne peut pas être négatif")
        Float Co,

        @PositiveOrZero(message = "Le cuivre ne peut pas être négatif")
        Float Cu,

        @PositiveOrZero(message = "Le fer ne peut pas être négatif")
        Float Fe,

        @PositiveOrZero(message = "Le fluor ne peut pas être négatif")
        Float F,

        @PositiveOrZero(message = "L'iode ne peut pas être négatif")
        Float I,

        @PositiveOrZero(message = "Le magnésium ne peut pas être négatif")
        Float Mg,

        @PositiveOrZero(message = "Le manganèse ne peut pas être négatif")
        Float Mn,

        @PositiveOrZero(message = "Le molybdène ne peut pas être négatif")
        Float Mo,

        @PositiveOrZero(message = "Le sodium ne peut pas être négatif")
        Float Na,

        @PositiveOrZero(message = "Le phosphore ne peut pas être négatif")
        Float P,

        @PositiveOrZero(message = "Le potassium ne peut pas être négatif")
        Float K,

        @PositiveOrZero(message = "Le rubidium ne peut pas être négatif")
        Float Rb,

        @PositiveOrZero(message = "Le silicium ne peut pas être négatif")
        Float SiO,

        @PositiveOrZero(message = "Le soufre ne peut pas être négatif")
        Float S,

        @PositiveOrZero(message = "Le sélénium ne peut pas être négatif")
        Float Se,

        @PositiveOrZero(message = "Le vanadium ne peut pas être négatif")
        Float V,

        @PositiveOrZero(message = "L'étain ne peut pas être négatif")
        Float Sn,

        @PositiveOrZero(message = "Le zinc ne peut pas être négatif")
        Float Zn,

        // Champs optionnels sans validation stricte
        String codeBarre,
        String marque
) {
}