public class Utility {
    DataManager dm;

    public Utility(DataManager dm){

        this.dm = dm;
    }

    void binarization(DataManager rect, int i){
        int h, w;
        h=rect.img.getHeight();   //Returns the height in pixels of the Raster
        w=rect.img.getWidth();    //

        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++){

                if(dm.img.getRaster().getSample(wi,hi,0)<=i){
                    dm.img.getRaster().setSample(wi,hi,0,0);
                }
                else{
                    dm.img.getRaster().setSample(wi,hi,0,255);
                }
            }
        }
    }
    //getSample - Returns the sample in a specified band for the pixel located at (x,y) as an int
    //b - color channel, w grayscale ma byc 0
    //i - wpisana wartosc binaryzacji
    //s=0 -> czarny
    //s=255 -> bialy

    void brighter(DataManager rect, int i)
    {
        int h, w;
        h=rect.img.getHeight();
        w=rect.img.getWidth();
        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++) {

                if(dm.img.getRaster().getSample(wi,hi,0)+i>=255){
                    dm.img.getRaster().setSample(wi,hi,0,255);
                }
                else {
                    dm.img.getRaster().setSample(wi,hi,0,dm.img.getRaster().getSample(wi,hi,0)+i);
                }
            }
        }
    }

    void darker(DataManager rect, int i)
    {
        int h, w;
        h=rect.img.getHeight();
        w=rect.img.getWidth();
        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++) {

                if(dm.img.getRaster().getSample(wi,hi,0)-i<0){dm.img.getRaster().setSample(wi,hi,0,0);}
                else {dm.img.getRaster().setSample(wi,hi,0,dm.img.getRaster().getSample(wi,hi,0)-i);}
            }
        }
    }

    int[] getNeighbors(int Width, int Height) {   //maska, sasiedzi srodkowego piksela

        int[] neighbors = new int[8];
        int licznik=0;

        for (int wi = Width-1; wi <= Width+1; wi++) {
            for (int hi = Height-1; hi <= Height+1; hi++) {

                if(wi==Width && hi==Height) { }

                else{neighbors[licznik]=dm.pixels[wi][hi]; licznik++;}
            }
        }
        return neighbors;
    }

    void downFilter(DataManager rect)
    {
        int h, w;
        h=rect.img.getHeight();   //Returns the height in pixels of the Raster
        w=rect.img.getWidth();

        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++) {
                dm.pixels[wi][hi]= dm.img.getRaster().getSample(wi,hi,0);
            }}

        for(int wi = 1; wi < (w)-1; wi++){
            for(int hi = 1; hi < (h)-1; hi++) {

                int middle=dm.img.getRaster().getSample(wi,hi,0)*(1/9); // 1/9 -->normalizacja

                int[] localNeighbor=getNeighbors(wi,hi);

                for(int i=0;i< localNeighbor.length;i++) {
                    middle = middle+(localNeighbor[i] * 1/9); // tez
                }

                if(middle>255){dm.img.getRaster().setSample(wi,hi,0,255);}
                else {dm.img.getRaster().setSample(wi,hi,0,middle);}

            }
        }
    }


    void upFilter(DataManager rect)
    {
        int h, w;
        h=rect.img.getHeight();   //Returns the height in pixels of the Raster
        w=rect.img.getWidth();

        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++) {
                dm.pixels[wi][hi]= dm.img.getRaster().getSample(wi,hi,0);
            }}


        for(int wi = 1; wi < (w)-1; wi++){
            for(int hi = 1; hi < (h)-1; hi++) {

                int middle=dm.img.getRaster().getSample(wi,hi,0)*9;

                int[] localNeighbor=getNeighbors(wi,hi);

                for(int i=0;i< localNeighbor.length;i++) {
                    middle = middle+(localNeighbor[i] * (-1));
                }

                if(middle<=0){dm.img.getRaster().setSample(wi,hi,0,0);}
                else if(middle>255){dm.img.getRaster().setSample(wi,hi,0,255);}
                else {dm.img.getRaster().setSample(wi,hi,0,middle);}

            }
        }
    }

    void gauss(DataManager rect)    //normalizacja 54 
    {
        int h, w;
        h=rect.img.getHeight();   //Returns the height in pixels of the Raster
        w=rect.img.getWidth();

        for(int wi = 0; wi < (w); wi++)
            for (int hi = 0; hi < (h); hi++) {
                dm.pixels[wi][hi] = dm.img.getRaster().getSample(wi, hi, 0);
            }


        for(int wi = 1; wi < (w)-1; wi++){
            for(int hi = 1; hi < (h)-1; hi++) {

                int middle=dm.img.getRaster().getSample(wi,hi,0)*32;

                int[] localNeighbor=getNeighbors(wi,hi);

                for(int i=0;i< localNeighbor.length;i++) {
                    if(i==0||i==2||i==6||i==8){middle = middle+((localNeighbor[i] * 1));}
                    else {middle = middle+((localNeighbor[i] * (2)));}
                }
                middle=middle/32;

                if(middle<0){dm.img.getRaster().setSample(wi,hi,0,0);}
                else if(middle>255){dm.img.getRaster().setSample(wi,hi,0,255);}
                else {dm.img.getRaster().setSample(wi,hi,0,middle);}
            }
        }
    }

    //Jeżeli choć jeden piksel z sąsiedztwa objętego przez element strukturalny ma wartość równą
    //zero, punkt centralny również otrzymuje wartość zero.
    void erozja(DataManager rect){
        int h, w;
        h=rect.img.getHeight();   //Returns the height in pixels of the Raster
        w=rect.img.getWidth();

        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++) {
                dm.pixels[wi][hi]= dm.img.getRaster().getSample(wi,hi,0);
            }}

        for(int wi = 1; wi < (w)-1; wi++){
            for(int hi = 1; hi < (h)-1; hi++) {
                int[] localNeighbor=getNeighbors(wi,hi);

                for(int i=0;i< localNeighbor.length;i++)
                {
                    if (localNeighbor[i] == 255) {
                        dm.img.getRaster().setSample(wi, hi, 0, 255);
                        break ;
                    }
                    else {}
                }
            }
        }
    }

    //Jeżeli choć jeden piksel z sąsiedztwa objętego przez element strukturalny ma wartość równą
    //jeden, punkt centralny również otrzymuje wartość jeden.
    void dylatacja(DataManager rect){
        int h, w;
        h=rect.img.getHeight();   //Returns the height in pixels of the Raster
        w=rect.img.getWidth();

        for(int wi = 0; wi < (w); wi++){
            for(int hi = 0; hi < (h); hi++) {
                dm.pixels[wi][hi]= dm.img.getRaster().getSample(wi,hi,0);
            }}

        for(int wi = 1; wi < (w)-1; wi++){
            for(int hi = 1; hi < (h)-1; hi++) {
                int[] localNeighbor=getNeighbors(wi,hi);

                for(int i=0;i< localNeighbor.length;i++)
                {
                    if (localNeighbor[i] == 0) {
                        dm.img.getRaster().setSample(wi, hi, 0, 0);
                        break ;
                    }
                    else {}
                }
            }
        }
    }

    void morfOpen(DataManager rect){   //nałożenie operacji dylatacji na wynik erozji obrazu pierwotnego
        erozja(rect);
        dylatacja(rect);
    }

    void morfClose(DataManager rect){     //nałożeniu operacji erozji na wynik dylatacji obrazu pierwotnego
        dylatacja(rect);
        erozja(rect);
    }


}