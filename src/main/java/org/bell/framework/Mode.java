package org.bell.framework;

import java.time.LocalTime;

/**
 * Created by hkn on 16.03.2016.
 */


public enum Mode {

    HOURS {
        @Override
        LocalTime increment(LocalTime time, int steps) {
            return time.plusHours(steps);
        }

        @Override
        void select(TimeSpinner spinner) {
            int index = spinner.getEditor().getText().indexOf(':');
            spinner.getEditor().selectRange(0, index);
        }
    },
    MINUTES {
        @Override
        LocalTime increment(LocalTime time, int steps) {
            return time.plusMinutes(steps);
        }

        @Override
        void select(TimeSpinner spinner) {
            int hrIndex = spinner.getEditor().getText().indexOf(':');
            int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
            spinner.getEditor().selectRange(hrIndex + 1, minIndex);
        }
    },
    SECONDS {
        @Override
        LocalTime increment(LocalTime time, int steps) {
            return time.plusSeconds(steps);
        }

        @Override
        void select(TimeSpinner spinner) {
            int index = spinner.getEditor().getText().lastIndexOf(':');
            spinner.getEditor().selectRange(index + 1, spinner.getEditor().getText().length());
        }
    };

    abstract LocalTime increment(LocalTime time, int steps);

    abstract void select(TimeSpinner spinner);

    LocalTime decrement(LocalTime time, int steps) {
        return increment(time, -steps);
    }
}
