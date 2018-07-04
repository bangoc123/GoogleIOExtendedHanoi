package com.quang.googleio.hanoi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.quang.googleio.hanoi.R
import com.quang.googleio.hanoi.app.AppController
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val account = GoogleSignIn.getLastSignedInAccount(this)

        imvClose.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            val firstname = edtFirstName.text.toString().trim()
            val lastname = edtLastName.text.toString().trim()
            val company = edtCompany.text.toString().trim()
            val info = edtProject.text.toString().trim()
            val birthday = edtBirthday.text.toString().trim()
            val exp = getExp(spnExp.selectedItemPosition)
            val occupation = getOccupation(spnOccupation.selectedItemPosition)
            val question = edtQuestion.text.trim()
            val phone = edtPhoneNumber.text.toString().trim()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && company.isNotEmpty() && birthday.isNotEmpty() &&
                    info.isNotEmpty() && info.isNotEmpty() && exp != null && occupation != null) {
                val params = JSONObject()
                params.put("firstname", firstname)
                params.put("lastname", lastname)
                params.put("company", company)
                params.put("info", info)
                params.put("exp", exp)
                params.put("occupation", occupation)
                params.put("phoneNumber", phone)
                if (question.isNotEmpty())
                    params.put("question", question)
                val regex = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)"
                val validbday = Pattern.compile(regex).matcher(birthday)
                if (validbday.matches()) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
                    val calender = Calendar.getInstance()
                    calender.time = sdf.parse(birthday)!!
                    params.put("day", calender.get(Calendar.DAY_OF_MONTH).toString())
                    params.put("month", (calender.get(Calendar.MONTH) + 1).toString())
                    params.put("year", calender.get(Calendar.YEAR).toString())
                    Log.d("data register", params.toString())
                    if (progressBar != null) progressBar.visibility = View.VISIBLE
                    val req = object : JsonObjectRequest(
                            Request.Method.POST,
                            "https://ioextended.org/api/apply",
                            params,
                            Response.Listener { response ->
                                Log.d("register", response.toString())
                                if (progressBar != null) progressBar.visibility = View.INVISIBLE
                                val returnIntent = Intent()
                                setResult(Activity.RESULT_OK, returnIntent)
                                finish()
                            },
                            Response.ErrorListener { error ->
                                error.printStackTrace()
                                Toast.makeText(this, "Register fail", Toast.LENGTH_LONG).show()
                                if (progressBar != null) progressBar.visibility = View.INVISIBLE
                            }
                    ) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val paramsHeader = HashMap<String, String>()
                            paramsHeader["Authorization"] = account!!.idToken!!
                            return paramsHeader
                        }
                    }
                    AppController.instance!!.addToRequestQueue(req)
                } else showMessage("Birthday is invalid")

            } else showMessage("Please input all field to register")
        }

        val listOccupation = resources.getStringArray(R.array.occupation)
        val listExp = resources.getStringArray(R.array.exp)
        val adapterOccupation = ArrayAdapter(this, R.layout.spinner_item, listOccupation)
        val adapterExp = ArrayAdapter(this, R.layout.spinner_item, listExp)
        spnOccupation.adapter = adapterOccupation
        spnExp.adapter = adapterExp
    }

    fun getExp(position: Int): String? {
        return when (position) {
            0 -> null
            1 -> "1"
            2 -> "3"
            3 -> "4"
            4 -> "5"
            else -> "6"
        }
    }

    fun getOccupation(position: Int): String? {
        return when (position) {
        //"dev", "pm", "goooglefan", "media", "other"
            0 -> null
            1 -> "dev"
            2 -> "pm"
            3 -> "goooglefan"
            4 -> "media"
            else -> "other"
        }
    }

    private fun showMessage(content: String, f: () -> Unit = {}) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(content)
        dialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            f()
        }
        dialog.create()
        if (!isFinishing) dialog.show()
    }
}
