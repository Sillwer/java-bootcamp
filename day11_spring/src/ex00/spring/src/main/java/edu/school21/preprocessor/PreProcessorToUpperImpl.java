package edu.school21.preprocessor;

public class PreProcessorToUpperImpl implements PreProcessor {
    @Override
    public Object process(Object obj) {
        return ((String) obj).toUpperCase();
    }
}
