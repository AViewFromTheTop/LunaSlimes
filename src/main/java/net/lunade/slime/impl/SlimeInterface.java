package net.lunade.slime.impl;

public interface SlimeInterface {

    float prevSquish();

    int getMergeCooldown();

    void setMergeCooldown(int i);

    float wobbleAnimProgress(float tickDelta);

    void playWobbleAnim();

    float getSizeScale(float tickDelta);

}
