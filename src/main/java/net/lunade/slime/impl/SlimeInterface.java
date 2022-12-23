package net.lunade.slime.impl;

public interface SlimeInterface {

    float prevSquish();

    int getMergeCooldown();

    void setMergeCooldown(int i);

    float splitAnimProgress(float tickDelta);

    void playSplitAnim();

    float getSizeScale(float tickDelta);

}
