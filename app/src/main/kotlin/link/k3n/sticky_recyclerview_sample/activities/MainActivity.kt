package link.k3n.sticky_recyclerview_sample.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import link.k3n.sticky_recyclerview_sample.R
import link.k3n.sticky_recyclerview_sample.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        Timber.d("Hello world!")
    }
}
