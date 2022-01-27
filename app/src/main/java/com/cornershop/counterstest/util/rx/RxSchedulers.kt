package gentera.yastas.yas_app_client_gestion_ventas.util.rx

import io.reactivex.Scheduler

interface RxSchedulers {

    fun runOnBackGround():Scheduler

    fun io():Scheduler

    fun compute():Scheduler

    fun androidThread():Scheduler

    fun internet():Scheduler
}