package wacode.yamada.yuki.nempaymentapp.view.activity

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment


abstract class BaseFragmentActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {
    abstract fun initialFragment(): BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.addOnBackStackChangedListener(this)
        replaceFragment(initialFragment(), false)
    }

    fun replaceFragment(fragment: BaseFragment, animated: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (animated) {
            fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left,
                    R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()

        setToolbarTitle(fragment.getTitleRes())
    }

    override fun onBackStackChanged() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as BaseFragment

        if (fragment != null) {
            setToolbarTitle(fragment.getTitleRes())
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return false
    }

    companion object {
        const val SIMPLE_FRAGMENT_ONLY_LAYOUT = R.layout.activity_container
    }
}