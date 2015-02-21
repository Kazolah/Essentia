package com.essentia.workout.workout_pojos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public abstract class Trigger implements TickComponent {
    ArrayList<Feedback> triggerAction = new ArrayList<Feedback>();
    final ArrayList<TriggerSuppression> triggerSuppression = new ArrayList<TriggerSuppression>();

    @Override
    public void onInit(Workout s) {
        for (Feedback f : triggerAction) {
            f.onInit(s);
        }
    }

    @Override
    public void onBind(Workout s, HashMap<String, Object> bindValues) {
        for (Feedback f : triggerAction) {
            f.onBind(s, bindValues);
        }
    }

    @Override
    public void onEnd(Workout s) {
        for (Feedback f : triggerAction) {
            f.onEnd(s);
        }
    }

    public void fire(Workout w) {
        for (TriggerSuppression s : triggerSuppression) {
            if (s.suppress(this, w)) {
                System.err.println("trigger: " + this + "suppressed by: " + s);
                return;
            }
        }
        for (Feedback f : triggerAction) {
            w.addFeedback(f);
        }
    }
}

