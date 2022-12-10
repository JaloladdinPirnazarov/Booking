package com.klimgroup.booking

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.klimgroup.booking.DataBase.*
import com.klimgroup.booking.databinding.ActivityMainBinding
import java.time.LocalTime

class MainActivity : AppCompatActivity(),Actions {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rcAdapter: RCAdapter
    private lateinit var dbManager: DbManager
    private lateinit var prefManager: ShPrefManager

    private val visible = View.VISIBLE
    private val gone = View.GONE

    private var isManager = true
    private var newTableIsVisible = false

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariables()
        setActivity()
        onClickListeners()
    }

    private fun initVariables(){
        dbManager = DbManager(this)
        dbManager.openDb()
        rcAdapter = RCAdapter(dbManager.getTables(),this)
        binding.rcTables.layoutManager = LinearLayoutManager(this)
        binding.rcTables.adapter = rcAdapter
        updateAdapter()
        prefManager = ShPrefManager(getSharedPreferences("MyPref", MODE_PRIVATE))
    }

    private fun setActivity(){
        binding.apply {
            contentContainer.visibility = gone
            loginContainer.visibility = gone
            val token = prefManager.getToken()
            Log.d("Token","username: ${token.username}")
            Log.d("Token","password: ${token.password}")
            if (token.username.isEmpty() || token.password.isEmpty()){
                roleContainer.visibility = gone
                registerContainer.visibility = visible
            }else{
                roleContainer.visibility = visible
                registerContainer.visibility = gone
            }
        }
    }

    private fun onClickListeners(){
        binding.apply {
            btnRegister.setOnClickListener { register() }
            btnLogIn.setOnClickListener { login() }
            manager.setOnClickListener {
                isManager = true
                changeRole()
            }
            customer.setOnClickListener {
                isManager = false
                changeRole()
            }
            imgBackLogin.setOnClickListener { setActivity() }
            btnNewTable.setOnClickListener { showHideNewTable() }
            btnAdd.setOnClickListener { saveTable() }
            tvRole.setOnClickListener {
                isManager = !isManager
                changeRole()
            }
            imgRole.setOnClickListener {
                isManager = !isManager
                changeRole()
            }

        }
    }

    private fun login(){
        binding.apply {
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()
            var correct = true
            if (username.isEmpty()){
                correct = false
                edUsername.error = "Fill the field"
            }
            if (password.isEmpty()){
                correct = false
                edPassword.error = "Fill the field"
            }
            if (correct){
                val token = prefManager.getToken()
                if (token.username == username && token.password == password){
                    loginContainer.visibility = gone
                    contentContainer.visibility = visible
                    btnNewTable.visibility = visible
                    newTableContainer.visibility = gone
                    tvRole.text = "Role: Manager"
                    imgRole.setImageResource(R.drawable.manager_boss)
                    edUsername.setText("")
                    edPassword.setText("")
                }else{
                    alertDialog(R.drawable.warning,"Error","Username or password is incorrect").apply {
                        setPositiveButton("ok"){_,_->}
                        show()
                    }
                }
            }
        }
    }

    private fun register(){
        var correct = true
        val token = Token()
        binding.apply {
            token.username = edRegisterUsername.text.toString()
            token.password = edRegisterPassword.text.toString()
            val conformPassword = edRegisterConiformPassword.text.toString()

            if (token.username.isEmpty()){
                correct = false
                edRegisterUsername.error = "Fill the field"
            }

            if (token.password.isEmpty()){
                correct = false
                edRegisterPassword.error = "Fill the field"
            }

            if (conformPassword.isEmpty()){
                correct = false
                edRegisterConiformPassword.error = "Fill the field"
            }

            if (token.password.isNotEmpty() &&
                conformPassword.isNotEmpty() &&
                token.password != conformPassword){
                correct = false
                edRegisterConiformPassword.error = "Incorrect password"
            }

            if (correct){
                edRegisterUsername.setText("")
                edRegisterPassword.setText("")
                edRegisterConiformPassword.setText("")
                registerContainer.visibility = gone
                changeRole()
                prefManager.saveToken(token)
            }
        }
    }

    private fun changeRole(){
        binding.apply {
            roleContainer.visibility = gone
            contentContainer.visibility = gone
            if (isManager){
                registerContainer.visibility = gone
                loginContainer.visibility = visible
            }

            else{
                tvRole.text = "Role: Customer"
                imgRole.setImageResource(R.drawable.people_customer)
                contentContainer.visibility = visible
                newTableContainer.visibility = gone
                btnNewTable.visibility = gone
            }


        }
    }

    private fun showHideNewTable(){
        binding.apply {
            if (newTableIsVisible){
                newTableContainer.visibility = gone
                newTableIsVisible = false
            }else{
                newTableContainer.visibility = visible
                newTableIsVisible = true
            }
        }
    }

    private fun saveTable(){
        binding.apply {
            var correct = true
            val number = edNumber.text.toString()
            val price = edCost.text.toString()
            if (number.isEmpty()){
                correct = false
                edNumber.error = "Fill tha field"
            }

            if (price.isEmpty()){
                correct = false
                edCost.error = "Fill the field"
            }

            if (correct){
                val table = TableItems()
                table.number = number.toInt()
                table.price = price.toInt()
                Log.d("table","save function: $table")
                dbManager.insertTable(table)
                updateAdapter()
                edNumber.setText("")
                edCost.setText("")
            }
        }
    }

    private fun toastMessage(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun alertDialog(icon:Int,title:String,message:String): AlertDialog.Builder {
        return AlertDialog.Builder(this).apply {
            setIcon(icon)
            setTitle(title)
            setMessage(message)
        }
    }

    private fun updateAdapter(){
        val list = dbManager.getTables()
        Log.d("table","update function: $list")
        binding.apply {
            if (list.size == 0){
                imgEmpty.visibility = visible
                tvEmpty.visibility = visible
            }else{
                imgEmpty.visibility = gone
                tvEmpty.visibility = gone
            }
        }
        rcAdapter.updateAdapter(list)
    }

    override fun delete(id: Int) {
        if (isManager){
            alertDialog(R.drawable.warning,"Warning","Are sure want to delete?").apply {
                setPositiveButton("yes"){_,_->
                    dbManager.deleteATable(id)
                    updateAdapter()
                }
                setNegativeButton("no"){_,_->}
                show()
            }
        }else toastMessage("you can't delete")
    }

    override fun book(table: TableItems) {
        dbManager.updateTable(table)
        updateAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun click(table: TableItems) {
        if (isManager){
            if (table.from == "free")
                toastMessage("free")
            else{
                alertDialog(R.drawable.warning,"Warning","Are you sure make this table free").apply {
                    setNegativeButton("No"){_,_->}
                    setPositiveButton("Yes"){_,_->
                        table.from = "free"
                        table.to = "free"
                        dbManager.updateTable(table)
                        updateAdapter()
                    }
                    show()
                }
            }
        }else{
            val dialog = TimePickerDialog(this,{_,hour,minute->
                val time = "$hour:$minute"
                if (time == table.from){
                    alertDialog(R.drawable.warning,"Error","This time already booked").apply {
                        setPositiveButton("ok"){_,_->}
                        show()
                    }
                }else{
                    table.from = time
                    table.to = "${time.substringBefore(":").toInt() + 1}:${time.substringAfter(":")}"
                    dbManager.updateTable(table)
                    updateAdapter()
                    toastMessage("Table booked successfully")
                }
            }, LocalTime.now().hour, LocalTime.now().minute,true)
            dialog.show()
        }
    }
}