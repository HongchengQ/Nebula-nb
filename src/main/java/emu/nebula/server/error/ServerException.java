package emu.nebula.server.error;

import lombok.Getter;
import emu.nebula.proto.Public.Error;

@Getter
public class ServerException extends Exception {
    private static final long serialVersionUID = -8953641375717705518L;
    private int code;
    private String[] args;
    
    public ServerException(int code) {
        this.code = code;
    }
    
    public ServerException(int code, String... args) {
        this.code = code;
        this.args = args;
    }
    
    // Proto
    
    public Error toProto() {
        var proto = Error.newInstance()
                .setCode(code);
        
        if (this.args != null) {
            proto.addAllArguments(args);
        }
        
        return proto;
    }
}
