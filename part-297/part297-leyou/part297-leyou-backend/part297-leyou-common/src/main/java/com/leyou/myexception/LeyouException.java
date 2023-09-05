package com.leyou.myexception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LeyouException extends RuntimeException{

    private ExceptionEnum exceptionEnum;

}
