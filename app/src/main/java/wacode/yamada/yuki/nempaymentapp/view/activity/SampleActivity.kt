package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import wacode.yamada.yuki.nempaymentapp.R

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, SampleActivity::class.java)
            return intent
        }
    }
}
