package net.lunade.slime.impl;

public interface SlimeInterface {

    float prevSquish();

    int getMergeCooldown();

    void setMergeCooldown(int i);

    float wobbleAnimProgress(float tickDelta);

    void playWobbleAnim();

    float getSizeScale(float tickDelta);

    void cheatSize(float f);

    void setJumpAntic(boolean bl);

    boolean getJumpAntic();

    void setJumpAnticTicks(int i);

    int getJumpDelay();

    void setJumpDelay(int i);

    float getDeathProgress(float f);

}
