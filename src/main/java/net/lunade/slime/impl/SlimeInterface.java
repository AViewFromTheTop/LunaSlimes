package net.lunade.slime.impl;

public interface SlimeInterface {

    float lunaSlimes$prevSquish();

    int lunaSlimes$getMergeCooldown();

    void lunaSlimes$setMergeCooldown(int i);

    float lunaSlimes$wobbleAnimProgress(float tickDelta);

    void lunaSlimes$playWobbleAnim();

    float lunaSlimes$getSizeScale(float tickDelta);

    void lunaSlimes$cheatSize(float f);

    void lunaSlimes$setJumpAntic(boolean bl);

    boolean lunaSlimes$getJumpAntic();

    void lunaSlimes$setJumpAnticTicks(int i);

    int lunaSlimes$getSavedJumpDelay();

    void lunaSlimes$setJumpDelay(int i);

    float lunaSlimes$getDeathProgress(float f);

    boolean lunaSlimes$canSquish();

    void lunaSlimes$setInWorld(boolean bl);

    boolean lunaSlimes$isInWorld();

}
