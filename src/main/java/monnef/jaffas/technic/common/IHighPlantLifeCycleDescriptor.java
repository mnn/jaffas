/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

public interface IHighPlantLifeCycleDescriptor {
    int getStagesCount();

    int generateStageLength(int stageNumber);

    boolean isFinalStage(int stage);
}
