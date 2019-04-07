package models.requests;

public class Requester {
    private ServiceRequest dan;
    private ServiceRequest danya;
    private ServiceRequest garrett;
    private ServiceRequest katie;
    private ServiceRequest manas;
    private ServiceRequest matt;
    private ServiceRequest max;
    private ServiceRequest niko;
    private ServiceRequest sophie;
    private ServiceRequest theo;

    public Requester() {
        dan = new Dan();
        danya = new Danya();
        garrett = new Garrett();
        katie = new Katie();
        manas = new Manas();
        matt = new Matt();
        max = new Max();
        niko = new Niko();
        sophie = new Sophie();
        theo = new Theo();
    }

    public String descDan() {
        return dan.getDescription();
    }

    public String descDanya() {
        return danya.getDescription();
    }

    public String descGarrett() {
        return garrett.getDescription();
    }

    public String descKatie() {
        return katie.getDescription();
    }

    public String descManas() {
        return manas.getDescription();
    }

    public String descMatt() {
        return matt.getDescription();
    }

    public String descMax() {
        return max.getDescription();
    }

    public String descNiko() {
        return niko.getDescription();
    }

    public String descSophie() {
        return sophie.getDescription();
    }

    public String descTheo() {
        return theo.getDescription();
    }
}
