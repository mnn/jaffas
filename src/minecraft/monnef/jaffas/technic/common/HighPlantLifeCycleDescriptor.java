/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.RandomHelper;

public class HighPlantLifeCycleDescriptor implements IHighPlantLifeCycleDescriptor {
    private static final int MIN_TO_TICK = 60 * 20;
    public int stagesCount;
    public int stageLenInMinutes;
    public int lengthMayDifferMaximallyByNPercent;

    public static IHighPlantLifeCycleDescriptor create(LifeCycleType type) {
        switch (type) {
            case ORDINAL:
                HighPlantLifeCycleDescriptor r = new HighPlantLifeCycleDescriptor();
                r.stagesCount = 4; // 0..3
                r.stageLenInMinutes = 5;
                r.lengthMayDifferMaximallyByNPercent = 10; //%
                return r;

            default:
                throw new RuntimeException("unknown life cycle prototype");
        }
    }

    @Override
    public int getStagesCount() {
        return stagesCount;
    }

    @Override
    public int generateStageLength(int stageNumber) {
        return Math.round(
                (stageLenInMinutes * MIN_TO_TICK)
                        * (
                        1f + RandomHelper.generateRandomFromSymmetricInterval(lengthMayDifferMaximallyByNPercent / 100f)
                )
        );
    }

    public enum LifeCycleType {
        ORDINAL
    }
}
