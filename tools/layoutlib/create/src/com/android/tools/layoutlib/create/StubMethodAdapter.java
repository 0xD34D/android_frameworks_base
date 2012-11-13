/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.layoutlib.create;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * This method adapter rewrites a method by discarding the original code and generating
 * a stub depending on the return type. Original annotations are passed along unchanged.
 */
<<<<<<< HEAD
class StubMethodAdapter extends MethodVisitor {

    private static String CONSTRUCTOR = "<init>";
    private static String CLASS_INIT = "<clinit>";

=======
class StubMethodAdapter implements MethodVisitor {

    private static String CONSTRUCTOR = "<init>";
    private static String CLASS_INIT = "<clinit>";
    
>>>>>>> 54b6cfa... Initial Contribution
    /** The parent method writer */
    private MethodVisitor mParentVisitor;
    /** The method return type. Can be null. */
    private Type mReturnType;
    /** Message to be printed by stub methods. */
    private String mInvokeSignature;
    /** Flag to output the first line number. */
    private boolean mOutputFirstLineNumber = true;
<<<<<<< HEAD
    /** Flag that is true when implementing a constructor, to accept all original
=======
    /** Flag that is true when implementing a constructor, to accept all original 
>>>>>>> 54b6cfa... Initial Contribution
     *  code calling the original super constructor. */
    private boolean mIsInitMethod = false;

    private boolean mMessageGenerated;
    private final boolean mIsStatic;
<<<<<<< HEAD
    private final boolean mIsNative;

    public StubMethodAdapter(MethodVisitor mv, String methodName, Type returnType,
            String invokeSignature, boolean isStatic, boolean isNative) {
        super(Opcodes.ASM4);
=======

    public StubMethodAdapter(MethodVisitor mv, String methodName, Type returnType,
            String invokeSignature, boolean isStatic) {
>>>>>>> 54b6cfa... Initial Contribution
        mParentVisitor = mv;
        mReturnType = returnType;
        mInvokeSignature = invokeSignature;
        mIsStatic = isStatic;
<<<<<<< HEAD
        mIsNative = isNative;

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        if (CONSTRUCTOR.equals(methodName) || CLASS_INIT.equals(methodName)) {
            mIsInitMethod = true;
        }
    }
<<<<<<< HEAD

    private void generateInvoke() {
        /* Generates the code:
         *  OverrideMethod.invoke("signature", mIsNative ? true : false, null or this);
         */
        mParentVisitor.visitLdcInsn(mInvokeSignature);
        // push true or false
        mParentVisitor.visitInsn(mIsNative ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        // push null or this
        if (mIsStatic) {
            mParentVisitor.visitInsn(Opcodes.ACONST_NULL);
        } else {
            mParentVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        }

        int sort = mReturnType != null ? mReturnType.getSort() : Type.VOID;
        switch(sort) {
        case Type.VOID:
            mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/android/tools/layoutlib/create/OverrideMethod",
                    "invokeV",
                    "(Ljava/lang/String;ZLjava/lang/Object;)V");
=======
    
    private void generateInvoke() {
        /* Generates the code:
         *  OverrideMethod.invoke("signature", this);
         */
        mParentVisitor.visitLdcInsn(mInvokeSignature);
        if (mIsStatic) {
            mParentVisitor.visitInsn(Opcodes.ACONST_NULL); // push null
        } else {
            mParentVisitor.visitVarInsn(Opcodes.ALOAD, 0); // push this
        }
        mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                "com/android/tools/layoutlib/create/OverrideMethod",
                "invoke",
                "(Ljava/lang/String;Ljava/lang/Object;)V");
    }
    
    private void generateReturn() {
        /* Generates one of, depending on the return type:
         *   return;
         *   return 0;
         *   return 0L;
         *   return 0.0f;
         *   return 0.0;
         *   return null;
         */
        switch(mReturnType != null ? mReturnType.getSort() : Type.VOID) {
        case Type.VOID:
>>>>>>> 54b6cfa... Initial Contribution
            mParentVisitor.visitInsn(Opcodes.RETURN);
            break;
        case Type.BOOLEAN:
        case Type.CHAR:
        case Type.BYTE:
        case Type.SHORT:
        case Type.INT:
<<<<<<< HEAD
            mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/android/tools/layoutlib/create/OverrideMethod",
                    "invokeI",
                    "(Ljava/lang/String;ZLjava/lang/Object;)I");
            switch(sort) {
            case Type.BOOLEAN:
                Label l1 = new Label();
                mParentVisitor.visitJumpInsn(Opcodes.IFEQ, l1);
                mParentVisitor.visitInsn(Opcodes.ICONST_1);
                mParentVisitor.visitInsn(Opcodes.IRETURN);
                mParentVisitor.visitLabel(l1);
                mParentVisitor.visitInsn(Opcodes.ICONST_0);
                break;
            case Type.CHAR:
                mParentVisitor.visitInsn(Opcodes.I2C);
                break;
            case Type.BYTE:
                mParentVisitor.visitInsn(Opcodes.I2B);
                break;
            case Type.SHORT:
                mParentVisitor.visitInsn(Opcodes.I2S);
                break;
            }
            mParentVisitor.visitInsn(Opcodes.IRETURN);
            break;
        case Type.LONG:
            mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/android/tools/layoutlib/create/OverrideMethod",
                    "invokeL",
                    "(Ljava/lang/String;ZLjava/lang/Object;)J");
            mParentVisitor.visitInsn(Opcodes.LRETURN);
            break;
        case Type.FLOAT:
            mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/android/tools/layoutlib/create/OverrideMethod",
                    "invokeF",
                    "(Ljava/lang/String;ZLjava/lang/Object;)F");
            mParentVisitor.visitInsn(Opcodes.FRETURN);
            break;
        case Type.DOUBLE:
            mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/android/tools/layoutlib/create/OverrideMethod",
                    "invokeD",
                    "(Ljava/lang/String;ZLjava/lang/Object;)D");
=======
            mParentVisitor.visitInsn(Opcodes.ICONST_0);
            mParentVisitor.visitInsn(Opcodes.IRETURN);
            break;
        case Type.LONG:
            mParentVisitor.visitInsn(Opcodes.LCONST_0);
            mParentVisitor.visitInsn(Opcodes.LRETURN);
            break;
        case Type.FLOAT:
            mParentVisitor.visitInsn(Opcodes.FCONST_0);
            mParentVisitor.visitInsn(Opcodes.FRETURN);
            break;
        case Type.DOUBLE:
            mParentVisitor.visitInsn(Opcodes.DCONST_0);
>>>>>>> 54b6cfa... Initial Contribution
            mParentVisitor.visitInsn(Opcodes.DRETURN);
            break;
        case Type.ARRAY:
        case Type.OBJECT:
<<<<<<< HEAD
            mParentVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/android/tools/layoutlib/create/OverrideMethod",
                    "invokeA",
                    "(Ljava/lang/String;ZLjava/lang/Object;)Ljava/lang/Object;");
            mParentVisitor.visitTypeInsn(Opcodes.CHECKCAST, mReturnType.getInternalName());
            mParentVisitor.visitInsn(Opcodes.ARETURN);
            break;
        }

    }

    private void generatePop() {
        /* Pops the stack, depending on the return type.
         */
        switch(mReturnType != null ? mReturnType.getSort() : Type.VOID) {
        case Type.VOID:
            break;
        case Type.BOOLEAN:
        case Type.CHAR:
        case Type.BYTE:
        case Type.SHORT:
        case Type.INT:
        case Type.FLOAT:
        case Type.ARRAY:
        case Type.OBJECT:
            mParentVisitor.visitInsn(Opcodes.POP);
            break;
        case Type.LONG:
        case Type.DOUBLE:
            mParentVisitor.visitInsn(Opcodes.POP2);
            break;
        }
    }

    /* Pass down to visitor writer. In this implementation, either do nothing. */
    @Override
=======
            mParentVisitor.visitInsn(Opcodes.ACONST_NULL);
            mParentVisitor.visitInsn(Opcodes.ARETURN);
            break;
        }
    }

    /* Pass down to visitor writer. In this implementation, either do nothing. */
>>>>>>> 54b6cfa... Initial Contribution
    public void visitCode() {
        mParentVisitor.visitCode();
    }

    /*
     * visitMaxs is called just before visitEnd if there was any code to rewrite.
     * For non-constructor, generate the messaging code and the return statement
     * if it hasn't been done before.
     */
<<<<<<< HEAD
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (!mIsInitMethod && !mMessageGenerated) {
            generateInvoke();
=======
    public void visitMaxs(int maxStack, int maxLocals) {
        if (!mIsInitMethod && !mMessageGenerated) {
            generateInvoke();
            generateReturn();
>>>>>>> 54b6cfa... Initial Contribution
            mMessageGenerated = true;
        }
        mParentVisitor.visitMaxs(maxStack, maxLocals);
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * End of visiting.
     * For non-constructor, generate the messaging code and the return statement
     * if it hasn't been done before.
     */
<<<<<<< HEAD
    @Override
    public void visitEnd() {
        if (!mIsInitMethod && !mMessageGenerated) {
            generateInvoke();
=======
    public void visitEnd() {
        if (!mIsInitMethod && !mMessageGenerated) {
            generateInvoke();
            generateReturn();
>>>>>>> 54b6cfa... Initial Contribution
            mMessageGenerated = true;
            mParentVisitor.visitMaxs(1, 1);
        }
        mParentVisitor.visitEnd();
    }

    /* Writes all annotation from the original method. */
<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return mParentVisitor.visitAnnotation(desc, visible);
    }

    /* Writes all annotation default values from the original method. */
<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public AnnotationVisitor visitAnnotationDefault() {
        return mParentVisitor.visitAnnotationDefault();
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc,
            boolean visible) {
        return mParentVisitor.visitParameterAnnotation(parameter, desc, visible);
    }

    /* Writes all attributes from the original method. */
<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitAttribute(Attribute attr) {
        mParentVisitor.visitAttribute(attr);
    }

    /*
     * Only writes the first line number present in the original code so that source
     * viewers can direct to the correct method, even if the content doesn't match.
     */
<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitLineNumber(int line, Label start) {
        if (mIsInitMethod || mOutputFirstLineNumber) {
            mParentVisitor.visitLineNumber(line, start);
            mOutputFirstLineNumber = false;
        }
    }

    /**
     * For non-constructor, rewrite existing "return" instructions to write the message.
     */
<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitInsn(int opcode) {
        if (mIsInitMethod) {
            switch (opcode) {
            case Opcodes.RETURN:
            case Opcodes.ARETURN:
            case Opcodes.DRETURN:
            case Opcodes.FRETURN:
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
<<<<<<< HEAD
                // Pop the last word from the stack since invoke will generate its own return.
                generatePop();
                generateInvoke();
                mMessageGenerated = true;
                //$FALL-THROUGH$
            default:
                mParentVisitor.visitInsn(opcode);
            }
        }
    }

    @Override
=======
                generateInvoke();
                mMessageGenerated = true;
            }
            mParentVisitor.visitInsn(opcode);
        }
    }

>>>>>>> 54b6cfa... Initial Contribution
    public void visitLabel(Label label) {
        if (mIsInitMethod) {
            mParentVisitor.visitLabel(label);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        if (mIsInitMethod) {
            mParentVisitor.visitTryCatchBlock(start, end, handler, type);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (mIsInitMethod) {
            mParentVisitor.visitMethodInsn(opcode, owner, name, desc);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        if (mIsInitMethod) {
            mParentVisitor.visitFieldInsn(opcode, owner, name, desc);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        if (mIsInitMethod) {
            mParentVisitor.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitIincInsn(int var, int increment) {
        if (mIsInitMethod) {
            mParentVisitor.visitIincInsn(var, increment);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitIntInsn(int opcode, int operand) {
        if (mIsInitMethod) {
            mParentVisitor.visitIntInsn(opcode, operand);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitJumpInsn(int opcode, Label label) {
        if (mIsInitMethod) {
            mParentVisitor.visitJumpInsn(opcode, label);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitLdcInsn(Object cst) {
        if (mIsInitMethod) {
            mParentVisitor.visitLdcInsn(cst);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitLocalVariable(String name, String desc, String signature,
            Label start, Label end, int index) {
        if (mIsInitMethod) {
            mParentVisitor.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        if (mIsInitMethod) {
            mParentVisitor.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitMultiANewArrayInsn(String desc, int dims) {
        if (mIsInitMethod) {
            mParentVisitor.visitMultiANewArrayInsn(desc, dims);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        if (mIsInitMethod) {
            mParentVisitor.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitTypeInsn(int opcode, String type) {
        if (mIsInitMethod) {
            mParentVisitor.visitTypeInsn(opcode, type);
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void visitVarInsn(int opcode, int var) {
        if (mIsInitMethod) {
            mParentVisitor.visitVarInsn(opcode, var);
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
}
