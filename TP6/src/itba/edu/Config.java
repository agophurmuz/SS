package itba.edu;

import itba.edu.models.Particle;
import itba.edu.models.Position;

/* default */  class Config {

    /* default */ double L = 20;
    /* default */ double W = 20;
    /* default */ boolean open = true;
    /* default */ double D = 1.2;
    /* default */ int framesToPrint = 200;
    /* default */ double maxDiameter = 0.7;
    /* default */ double minDiameter = 0.5;
    /* default */ double particlesMass = 80.0;
    /* default */ int M = 15;
    /* default */ double rc = maxDiameter;
    /* default */ double k = 1E5;
    /* default */ double gama = 2 * Math.sqrt(k * particlesMass);
    /* default */ double totalTime = 10.0;
    /* default */ double deltaTime = 1E-5;
    /* default */ double delta2 = 0.001;
    /* default */ double accelerationTime = 0.5;
    /* default */ double A = 2000;
    /* default */ double B = 0.08;
    /* default */ double desiredV = 6.0;
    /* default */ Particle target = new Particle(1000, new Position<>(W / 2, -L / 10), 0.0, 0.0, 0.0, 0.0, 0);
    /* default */ int N = 5;

    /* default */ void setL(double l) {
        L = l;
    }

    /* default */ void setW(double w) {
        W = w;
    }

    /* default */ void setOpen(boolean open) {
        this.open = open;
    }

    /* default */ void setD(double d) {
        D = d;
    }

    /* default */ void setFramesToPrint(int framesToPrint) {
        this.framesToPrint = framesToPrint;
    }

    /* default */ void setMaxDiameter(double maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    /* default */ void setMinDiameter(double minDiameter) {
        this.minDiameter = minDiameter;
    }

    /* default */ void setParticlesMass(double particlesMass) {
        this.particlesMass = particlesMass;
    }

    /* default */ void setM(int m) {
        M = m;
    }

    /* default */ void setRc(double rc) {
        this.rc = rc;
    }

    /* default */ void setK(double k) {
        this.k = k;
    }

    /* default */ void setGama(double gama) {
        this.gama = gama;
    }

    /* default */ void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    /* default */ void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    /* default */ void setDelta2(double delta2) {
        this.delta2 = delta2;
    }

    /* default */ void setAccelerationTime(double accelerationTime) {
        this.accelerationTime = accelerationTime;
    }

    /* default */ void setA(double a) {
        A = a;
    }

    /* default */ void setB(double b) {
        B = b;
    }

    /* default */ void setDesiredV(double desiredV) {
        this.desiredV = desiredV;
    }

    /* default */ void setTarget(Particle target) {
        this.target = target;
    }

    /* default */ void setN(int n) {
        N = n;
    }

    /* default */ double getL() {
        return L;
    }

    /* default */ double getW() {
        return W;
    }

    /* default */ boolean isOpen() {
        return open;
    }

    /* default */ double getD() {
        return D;
    }

    /* default */ int getFramesToPrint() {
        return framesToPrint;
    }

    /* default */ double getMaxDiameter() {
        return maxDiameter;
    }

    /* default */ double getMinDiameter() {
        return minDiameter;
    }

    /* default */ double getParticlesMass() {
        return particlesMass;
    }

    /* default */ int getM() {
        return M;
    }

    /* default */ double getRc() {
        return rc;
    }

    /* default */ double getK() {
        return k;
    }

    /* default */ double getGama() {
        return gama;
    }

    /* default */ double getTotalTime() {
        return totalTime;
    }

    /* default */ double getDeltaTime() {
        return deltaTime;
    }

    /* default */ double getDelta2() {
        return delta2;
    }

    /* default */ double getAccelerationTime() {
        return accelerationTime;
    }

    /* default */ double getA() {
        return A;
    }

    /* default */ double getB() {
        return B;
    }

    /* default */ double getDesiredV() {
        return desiredV;
    }

    /* default */ Particle getTarget() {
        return target;
    }

    /* default */ int getN() {
        return N;
    }
}
