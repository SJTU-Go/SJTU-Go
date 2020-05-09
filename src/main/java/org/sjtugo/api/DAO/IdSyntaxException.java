package org.sjtugo.api.DAO;

public class IdSyntaxException extends RuntimeException {
    IdSyntaxException(String idtype){
        super("The format of your ID doesn't match " + idtype);
    }
}