package com.wyjax.withstudy.web.common.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class TimeConvert {
    public String timeConvert(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
