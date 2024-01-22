package frc.robot.control;

public class JoystickRequester {
    private double rumble;
    private JoystickProvider provider;

    public JoystickRequester(JoystickProvider provider){
        this.provider = provider;
        provider.requesters.add(this);
    }

    public void SetRumble(double value){
        rumble = value;
    }

    public double GetRumble(){
        return rumble;
    }

    public void SetActiveProvider(JoystickProvider provider){
        this.provider.requesters.remove(this);
        this.provider = provider;
        this.provider.requesters.add(this);
    }

    public double GetAxis(int axis){
        return provider.GetAxisWithDeadzone(axis);
    }

    public boolean GetButton(int button){
        return provider.GetButton(button);
    }
}
