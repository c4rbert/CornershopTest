package gentera.yastas.yas_app_client_gestion_ventas.util.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppRxSchedulers : RxSchedulers {
    override fun runOnBackGround(): Scheduler {
        return backgroundSchedulers
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun compute(): Scheduler {
        return Schedulers.computation()
    }

    override fun androidThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun internet(): Scheduler {
        return internetSchedulers
    }

    companion object{
        var backgroundExecutor: Executor = Executors.newCachedThreadPool()
        var backgroundSchedulers = Schedulers.from(backgroundExecutor)
        var internetExecutor: Executor = Executors.newCachedThreadPool()
        var internetSchedulers = Schedulers.from(internetExecutor)
    }
}