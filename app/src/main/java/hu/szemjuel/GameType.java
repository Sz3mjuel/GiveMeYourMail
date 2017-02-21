package hu.szemjuel;

public interface GameType {

    enum Type {
        TYPE1{
            @Override
            public String toString() {
                return "1";
            }
        },
        TYPE2{
            @Override
            public String toString() {
                return "2";
            }
        },
        TYPE3{
            @Override
            public String toString() {
                return "3";
            }
        }, TYPE4{
            @Override
            public String toString() {
                return "4";
            }
        };
    }
}
