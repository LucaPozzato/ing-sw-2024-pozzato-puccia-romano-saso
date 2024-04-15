package it.polimi.ingsw.codexnaturalis.utils;

import java.util.ArrayList;
//volendo utilizzare questo pattern il model e la view dovranno essere sottoclassi di ObservableImpl
//interfaccia Client sarà observer della view e passerà infromazioni tramite network al controller
//interfaccia Server sarà observer del model e passerà informazioni tramite network alla view
//le informazioni da passare dovranno essere una copia immutabile (e quindi serializzabile)
public abstract class ObservableImpl {

    private final ArrayList<Observer> observers;
//    ChatGpt sull'uso di vector:
//    Vector è stato scelto probabilmente per garantire la sincronizzazione dei thread,
//    poiché è intrinsecamente sincronizzato. Tuttavia, nota che Vector è considerato obsoleto,
//    e ArrayList con gestione della sincronizzazione esplicita potrebbe essere una scelta migliore in un contesto moderno.
    public ObservableImpl(){ observers = new ArrayList<>();}

    public synchronized void addObserver(Observer observer) throws NullPointerException {
        if ( observer == null ) throw new NullPointerException();
        if (!observers.contains(observer)) observers.add(observer);
    }

//    public void notifyObservers(/*Message message*/){
//
//        for (Observer observer : observers){
//            try{
//
//            } catch (e){
//                e.printStackTrace();
//            }
//        }
//    } //da capire quale forma vogliamo dare ai messaggi
//

}
