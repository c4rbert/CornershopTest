package gentera.yastas.yas_app_client_gestion_ventas.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cornershop.counterstest.data.repositories.creatcounter.CreateItemRepository
import com.cornershop.counterstest.data.repositories.main.MainRepository
import com.cornershop.counterstest.viewmodel.CreateItemViewModel
import com.cornershop.counterstest.viewmodel.MainViewModel


class ViewModelFactory(private val repo: Any) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return modelClass.getConstructor(MainRepository::class.java).newInstance(repo)
        }
        if (modelClass.isAssignableFrom(CreateItemViewModel::class.java)) {
            return modelClass.getConstructor(CreateItemRepository::class.java).newInstance(repo)
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}