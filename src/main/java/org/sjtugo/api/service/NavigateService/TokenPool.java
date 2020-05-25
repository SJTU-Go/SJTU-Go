package org.sjtugo.api.service.NavigateService;

public class TokenPool {
    int idx = 0;
    String[] tokens = new String[]{
        "WNKBZ-XTW3W-DAKRJ-OCPIH-ZQWNO-RRFB2",
        "BHBBZ-RTA3U-ICAVZ-23DAQ-C4BQ3-V7FCX",
        "I6IBZ-BCZRI-FHYGG-523D4-3W3C7-X6BRS",
        "WIJBZ-BWR3P-Z4BDT-LAHER-OYYDS-SPFEH",
    };
    int tokenNum = 4;

    public String getToken() {
        idx = (idx + 1)%tokenNum;
        return tokens[idx];
    }
}