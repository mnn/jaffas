/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

public interface IWindObstacles {
    void reset();

    int getTotalRadius();

    int getTotalDiameter();

    void compute();

    float debugCompute();

    float getObstaclesVolumeWorstScenario();

    float getObstaclesVolumeCached();
}
