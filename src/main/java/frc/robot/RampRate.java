package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class RampRate implements Sendable {
    
    private double m_lastValue, m_upRampRate, m_downRampRate;
    private double m_maxErrorToDesired;
    private double m_scaledError;

    public RampRate() {
        m_lastValue = 0;
    }

    public void setRampRates(double upRampRate, double downRampRate) {
        m_upRampRate = upRampRate;
        m_downRampRate = downRampRate;
    }

    public void setUpRampRate(double upRampRate) {
        m_upRampRate = upRampRate;
    }

    public void setDownRampRate(double downRampRate) {
        m_downRampRate = downRampRate;
    }

    public double getUpRampRate() {
        return m_upRampRate;
    }

    public double getDownRampRate(){
        return m_downRampRate;
    }

    public double getNextValue(double target) {
        if ((m_lastValue > 0 && target < 0) || (m_lastValue < 0 && target > 0)) {
            target = 0; // makes sure desired and lastValue have the same sign to make math easy
        }
        //System.out.println("Desired: " + desired);
        double errorToDesired = Math.abs(target - m_lastValue);

        if (errorToDesired > m_maxErrorToDesired) {
            m_maxErrorToDesired = errorToDesired;
        }

        m_scaledError = errorToDesired / m_maxErrorToDesired;

            if (Math.abs(target) > Math.abs(m_lastValue)) {
                if (Math.abs(target - m_lastValue) > m_upRampRate) {
                    if (target > 0) {
                        m_lastValue += m_upRampRate;
                    } else {
                        m_lastValue -= m_upRampRate;
                    }
                } else {
                    m_lastValue = target;
                }
            } else {
                if (Math.abs(target - m_lastValue) > m_downRampRate) {
                    if (m_lastValue > 0) {
                        m_lastValue -= m_downRampRate;
                    } else {
                        m_lastValue += m_downRampRate;
                    }
                } else {
                    m_lastValue = target;
                }
            }

            return m_lastValue;
    }

    /**
     * Used to immediately set the last value, potentially not following ramp rate
     * @param lastValue new value to be treated as the last value
     */
    public void setLastValue(double lastValue) {
        m_lastValue = lastValue;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("up ramp rate", this::getUpRampRate, this::setUpRampRate );
        builder.addDoubleProperty("down ramp rate", this::getDownRampRate, this::setDownRampRate);

    }
}