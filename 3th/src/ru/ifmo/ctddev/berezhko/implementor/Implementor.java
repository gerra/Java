package ru.ifmo.ctddev.berezhko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.*;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 05.03.15.
 */
public class Implementor implements Impler {

    private String getParametersAsString(Parameter[] params) {
        String asString = "";
        for (Parameter param : params) {
            String modifiers = Modifier.toString(param.getModifiers());
            if (modifiers.length() != 0) {
                modifiers += " ";
            }
            asString += (modifiers + param.getType().getCanonicalName() + " " + param.getName());
            if (param != params[params.length - 1]) {
                asString += ", ";
            }
        }
        return asString;
    }

    private String getThrowsAsString(Executable executable) {
        String asString = "";
        Class[] exceptions = executable.getExceptionTypes();
        if (exceptions.length > 0) {
            asString = " throws ";
            for (Class exception : exceptions) {
                asString += exception.getCanonicalName();
                if (exception != exceptions[exceptions.length - 1]) {
                    asString += ", ";
                }
            }
        }
        return asString;
    }

    private File makePackageDirs(Class clazz, File file) throws IOException {
        String packagePath = "/" + clazz.getPackage().getName().replace('.', '/');
        String classPath = file.getAbsoluteFile() + packagePath + "/" + clazz.getSimpleName() + "Impl.java";
        File newClassFile = new File(classPath);
        newClassFile.getParentFile().mkdirs();
        return newClassFile;
    }


    private void walkThoughMethods(Class clazz, MethodVisitor visitor) {
        if (clazz == null) {
            return;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            visitor.onVisit(method);
        }
        for (Class iface : clazz.getInterfaces()) {
            walkThoughMethods(iface, visitor);
        }
        walkThoughMethods(clazz.getSuperclass(), visitor);
    }

    private void writeMethodsToWriter(Class clazz, Writer writer) throws IOException {

        Map<String, Method> methods = new HashMap<>();

        // get all abstract methods ..
        walkThoughMethods(clazz, method -> {
            if (Modifier.isAbstract(method.getModifiers())) {
                String signature = method.getReturnType().getCanonicalName()
                        + method.getName()
                        + getParametersAsString(method.getParameters());
                if (!methods.containsKey(signature)) {
                    methods.put(signature, method);
                }
            }
        });

        // .. excluding methods with realisation
        walkThoughMethods(clazz, method -> {
            if (!Modifier.isAbstract(method.getModifiers())) {
                String signature = method.getReturnType().getCanonicalName()
                        + method.getName()
                        + getParametersAsString(method.getParameters());
                if (methods.containsKey(signature)) {
                    methods.remove(signature);
                }
            }
        });

        for (Method method : methods.values()) {
            String modifier = "";
            if (Modifier.isPublic(method.getModifiers())) {
                modifier = "public";
            } else if (Modifier.isProtected(method.getModifiers())) {
                modifier = "protected";
            }
            String signature = "";
            signature += modifier + " " + method.getReturnType().getCanonicalName() + " " + method.getName();
            signature += "(" + getParametersAsString(method.getParameters()) + ")" + getThrowsAsString(method);
            writer.write("    " + signature);
            writer.write(" {\n");
            writer.write("        return");

            if (method.getReturnType().isPrimitive()) {
                String returnType = method.getReturnType().getSimpleName();

                if (!returnType.equals("void")) {
                    writer.write(" ");
                }
                if (returnType.equals("short") || returnType.equals("int") || returnType.equals("byte")) {
                    writer.write("0");
                } else if (returnType.equals("long")) {
                    writer.write("0L");
                } else if (returnType.equals("float")) {
                    writer.write("0.0f");
                } else if (returnType.equals("double")) {
                    writer.write("0.0d");
                } else if (returnType.equals("char")) {
                    writer.write("0");
                } else if (returnType.equals("boolean")) {
                    writer.write("false");
                }
            } else {
                writer.write(" null");
            }
            writer.write(";\n");
            writer.write("    }\n\n");
        }

    }

    private void writeConstructorsToWriter(Class clazz, Writer writer) throws IOException, ImplerException {
        if (clazz.isInterface()) {
            return;
        }
        String name = clazz.getSimpleName() + "Impl";
        boolean hasConstructors = false;
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (Modifier.isPublic(constructor.getModifiers()) || Modifier.isProtected(constructor.getModifiers())) {
                hasConstructors = true;
                Parameter[] params = constructor.getParameters();
                String paramValuesAsString = "";
                for (Parameter parameter : params) {
                    paramValuesAsString += parameter.getName();
                    if (parameter != params[params.length - 1]) {
                        paramValuesAsString += ", ";
                    }
                }

                writer.write("    " + Modifier.toString(constructor.getModifiers() & Modifier.constructorModifiers()) + " " + name);
                writer.write("(" + getParametersAsString(params) + ")" + getThrowsAsString(constructor));
                writer.write(" {\n");
                writer.write("        super(" + paramValuesAsString + ");\n");
                writer.write("    }\n");
            }
        }
        if (!hasConstructors) {
            throw new ImplerException("No public or protected constructors found");
        }
    }

    private void writeClassToWriter(Class clazz, Writer writer) throws IOException, ImplerException {
        if (clazz.getPackage() != null) {
            writer.write("package " + clazz.getPackage().getName() + ";\n\n");
        }

        String extOrImpl = clazz.isInterface() ? " implements " : " extends ";

        writer.write("public class " + clazz.getSimpleName() + "Impl" + extOrImpl + clazz.getSimpleName());
        writer.write(" {\n");
        writeConstructorsToWriter(clazz, writer);
        writeMethodsToWriter(clazz, writer);
        writer.write("}");

    }

    @Override
    public void implement(Class<?> aClass, File file) throws ImplerException {
        if (aClass == null || aClass.isPrimitive() || Modifier.isFinal(aClass.getModifiers())) {
            throw new ImplerException();
        }

        File newClassFile;
        try {
            newClassFile = makePackageDirs(aClass, file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImplerException("Can't create new class file");
        }

        try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newClassFile)));) {
            writeClassToWriter(aClass, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private interface MethodVisitor {
        public void onVisit(Method method);
    }
}
