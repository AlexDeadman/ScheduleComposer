package com.example.auddistandroid

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.auddistandroid.adapter.LecturersAdapter
import com.example.auddistandroid.common.Common
import com.example.auddistandroid.databinding.ActivityMainBinding
import com.example.auddistandroid.model.lecturers.LecturersList
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // ----------------------------------------------------------------------------

        val lecturersCall = Common.retrofitService.getLecturersCall()

        lecturersCall.enqueue(object : Callback<LecturersList> {
            override fun onResponse(call: Call<LecturersList>, response: Response<LecturersList>) {
                val lecturers = response.body()!!

                val list = findViewById<RecyclerView>(R.id.recycler_lecturers_list)

                val adapter = LecturersAdapter(this@MainActivity, lecturers)
                list.adapter = adapter
            }

            override fun onFailure(call: Call<LecturersList>, t: Throwable) {
                val errorMsg = "Can't load lecturers "
                Log.e(errorMsg, t.message.toString())
                Toast.makeText(this@MainActivity, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }


}