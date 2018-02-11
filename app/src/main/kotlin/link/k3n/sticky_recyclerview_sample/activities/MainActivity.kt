package link.k3n.sticky_recyclerview_sample.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import link.k3n.sticky_recyclerview_sample.R
import link.k3n.sticky_recyclerview_sample.adapters.RecyclerViewAdapter
import link.k3n.sticky_recyclerview_sample.databinding.ActivityMainBinding
import link.k3n.sticky_recyclerview_sample.viewmodels.MainViewModel
import link.k3n.sticky_recyclerview_sample.views.custom.StickyHeaderItemDecoration
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Hello world!")
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        val adapter = RecyclerViewAdapter()
        val vm = MainViewModel()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(StickyHeaderItemDecoration(adapter))

        val list = ArrayList<MainViewModel.BaseListItem>()
        list.add(vm.getHeaderListItem("1"))
        list.add(vm.getContentListItem("1"))
        list.add(vm.getContentListItem("2"))
        list.add(vm.getHeaderListItem("2"))
        list.add(vm.getContentListItem("3"))
        list.add(vm.getContentListItem("4"))

        adapter.setItems(list)
    }
}
