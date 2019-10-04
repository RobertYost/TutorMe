package com.example.tutorme

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var createAccountButton: Button
    private lateinit var facebookLogInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        print("Created Login Activity")

        createAccountButton = findViewById(R.id.create_account_button)
        facebookLogInButton = findViewById(R.id.facebook_log_in_button)

        createAccountButton.setOnClickListener { view: View ->

            val intent = Intent(this, RegistrationActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
        facebookLogInButton.setOnClickListener { view: View ->
            //TODO: log in with facebook
        }
        //setSupportActionBar(toolbar)
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
         */
    }

    override fun onPause() {
        super.onPause()
        print("Paused Login Activity")
    }

    override fun onResume() {
        super.onResume()
        print("Resumed Login Activity")
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

     */
}
