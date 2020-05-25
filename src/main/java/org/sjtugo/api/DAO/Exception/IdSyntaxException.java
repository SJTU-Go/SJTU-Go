package org.sjtugo.api.DAO.Exception;

public class IdSyntaxException extends RuntimeException {
    IdSyntaxException(String idtype){
        super("The format of your ID doesn't match " + idtype);
    }
}