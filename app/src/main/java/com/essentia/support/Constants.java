package com.essentia.support;

import com.example.kyawzinlatt94.essentia.R;

import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public interface Constants {
    public interface DIMENSION{
        public static final int TIME = 1;
        public static final int DISTANCE = 2;
        public static final int SPEED = 3;
        public static final int PACE = 4;
        public static final int HR = 5;
        public static final int HRZ = 6;
    }

    public interface TRACKER_STATE {
        public static final int INIT = 0;         // initial state
        public static final int INITIALIZING = 1; // initializing components
        public static final int INITIALIZED = 2;  // initialized
        public static final int STARTED = 3;      // Workout started
        public static final int PAUSED = 4;       // Workout paused
        public static final int CLEANUP = 5;      // Cleaning up components
        public static final int ERROR = 6;        // Components failed to initialize ;
        public static final int CONNECTING = 7;
        public static final int CONNECTED = 8;
        public static final int STOPPED = 9;
    }
    public interface Intents {
        final String PAUSE_RESUME = "com.essentia.PAUSE_RESUME";
        final String NEW_LAP = "com.essentia.NEW_LAP";
        final String FROM_NOTIFICATION = "com.essentia.FROM_NOTIFICATION";
        final String START_WORKOUT = "com.essentia.START_WORKOUT";
        final String PAUSE_WORKOUT = "com.essentia.PAUSE_WORKOUT";
        final String RESUME_WORKOUT = "com.essentia.RESUME_WORKOUT";
    }
    public interface Images{
        final HashMap<String, Integer> RUNNING = new HashMap<String, Integer>(){{put("Running", R.drawable.running);}};
        final HashMap<String, Integer> RUNNING_TREADMILL = new HashMap<String, Integer>(){{put("Running, treadmill", R.drawable.running_treadmill);}};
        final HashMap<String, Integer> CYCLING = new HashMap<String, Integer>(){{put("Cycling", R.drawable.running);}};
        final HashMap<String, Integer> WALKING = new HashMap<String, Integer>(){{put("Walking", R.drawable.running);}};
        final HashMap<String, Integer> WALKING_TREADMILL = new HashMap<String, Integer>(){{put("Walking, treadmill", R.drawable.running);}};
        final HashMap<String, Integer> OTHER = new HashMap<String, Integer>(){{put("Other", R.drawable.running);}};
        final HashMap<String, Integer> OTHER_INDOOR = new HashMap<String, Integer>(){{put("Other, indoor", R.drawable.running);}};

        final HashMap<String, Integer> BASIC_WORKOUT = new HashMap<String, Integer>(){{put("Basic Workout", R.drawable.basic_workout);}};
        final HashMap<String, Integer> TARGET_GOAL = new HashMap<String, Integer>(){{put("Target Goal", R.drawable.target_chasing);}};
    }
}
