package edu.school21.preprocessor;

public class PreProcessorToLowerImpl implements PreProcessor {
    @Override
    public Object process(Object obj) {
        return ((String) obj).toLowerCase();
    }
}
