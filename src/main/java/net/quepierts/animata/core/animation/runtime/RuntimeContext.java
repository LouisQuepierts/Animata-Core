package net.quepierts.animata.core.animation.runtime;

public interface RuntimeContext {
    /**
     * Fetch the current value of the property
     * The dimension should be pOut.length
     *
     * @param pPath the path of the property
     * @param pOut  the array to store the value
     */
    void fetch(String pPath, float[] pOut);

    /**
     * Write the value of the property
     * The dimension should be pIn.length
     *
     * @param pPath the path of the property
     * @param pIn   the array to store the value
     */
    void write(String pPath, float[] pIn);

    /**
     * Get the current time
     * @return the current time
     */
    float getTime();
}
