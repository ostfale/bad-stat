package de.ostfale.qk.domain.points;

import java.util.Arrays;
import java.util.Optional;

public enum PointsTourType {
    E_RLT("E-RLT"),
    E_KM("E-KM"),
    D2_RLT("D2-RLT"),
    D2_BM("D2-BM"),
    D1_RLT("D1-RLT"),
    D1_BM("D1-BM"),
    C2_RLT("C2-RLT"),
    C2_LM("C2-LM"),
    C1_RLT("C1-RLT"),
    C1_LM("C1-LM"),
    B_RLT("B-RLT"),
    B_GM("B-GM"),
    A_RLT("A-RLT"),
    A_DM("A-DM"),
    GMF_U11("GMF-U11"),
    IS_U15("IS-U15"),
    U15_EM("U15-EM"),
    BEC_U17("BEC-U17"),
    U17_EM("U17-EM"),
    JFS_JIS_JIC_U19("JFS-JIS-JIC-U19"),
    JIGP_U19("JIGP-U19"),
    U19_EM("U19-EM"),
    JWM_YOG("JWM-YOG"),
    G3FS_O19("G3FS-O19"),
    G3IS_O19("G3IS-O19"),
    G3IC_O19("G3IC-O19"),
    G2L6_O19("G2L6-O19"),
    G2L5_O19("G2L5-O19"),
    G2L4_O19("G2L4-O19"),
    G2L3_O19("G2L3-O19"),
    G2L12_O19("G2L12-O19"),
    G1_O19("G1-O19");

    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    PointsTourType(String displayName) {
        this.displayName = displayName;
    }

    public static Optional<PointsTourType> fromDisplayName(String displayName) {
        return Arrays.stream(PointsTourType.values())
                .filter(type -> type.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst();
    }
}
