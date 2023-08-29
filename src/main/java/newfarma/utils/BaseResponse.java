package newfarma.utils;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class BaseResponse {

    private String code;
    private Integer status;
    private String message;
}
