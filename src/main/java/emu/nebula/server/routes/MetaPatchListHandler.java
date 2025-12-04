package emu.nebula.server.routes;

import org.jetbrains.annotations.NotNull;

import emu.nebula.server.HttpServer;
import emu.nebula.util.AeadHelper;
import emu.nebula.util.Utils;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PRIVATE)
public class MetaPatchListHandler implements Handler {
    private HttpServer server;
    
    public MetaPatchListHandler(HttpServer server) {
        this.server = server;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        // Get diff
        var diffBytes = this.getServer().getDiff();
        
        // Sanity check
        if (diffBytes == null) {
            ctx.contentType(ContentType.APPLICATION_OCTET_STREAM);
            ctx.result(Utils.EMPTY_BYTE_ARRAY);
            return;
        }
        
        // Encrypt patch list
        ctx.contentType(ContentType.APPLICATION_OCTET_STREAM);
        ctx.result(AeadHelper.encryptCBC(diffBytes));
    }

}
