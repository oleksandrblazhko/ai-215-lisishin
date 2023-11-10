// Код активності (MainActivity)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("HealthQuestion")

        binding.btn1.setOnClickListener {
            saveHealthQuestion()
        }
    }

    private fun saveHealthQuestion() {
        val questionText = binding.qstEd1.text.toString()

        if (questionText.isEmpty() || questionText.length > 500) {
            Toast.makeText(this, "Будь ласка, введіть питання", Toast.LENGTH_SHORT).show()
            return
        }

        val quesId = dbRef.push().key!!
        val question = HealthQuestion(quesId, questionText)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Збереження...")
        progressDialog.show()

        dbRef.child(quesId).setValue(question)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()

                val resultMessage = if (task.isSuccessful) {
                    "Питання успішно збережено"
                } else {
                    "Помилка: ${task.exception?.message}"
                }

                showResultDialog(resultMessage)
            }
    }

    private fun showResultDialog(resultMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(resultMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }
}

// Модель HealthQuestion
@Parcelize
data class HealthQuestion(
    var quesId: String? = null,
    var question: String? = null,
    var date: Date = Date()
) : Parcelable


Метод `saveHealthQuestion` у вищенаведеному коді призначений для збереження питань про здоров'я в базу даних Firebase Realtime Database. Давайте розглянемо приклади використання цього методу у контексті додатка:

1. **Користувач вводить нове питання про здоров'я:**
   Коли користувач запускає додаток і переходить до відповідного екрану, він бачить поле для введення питання про здоров'я. Введене питання може бути що завгодно, наприклад, "Як впливає здоров'я на роботу організму?".

2. **Користувач натискає кнопку "Надіслати" (або аналогічну):**
   Після введення питання користувач натискає кнопку "Надіслати". Це призводить до виклику методу `saveHealthQuestion`, який розпочинає процес збереження питання в базі даних.

3. **Запускається ProgressDialog:**
   Під час збереження питання в базі даних виводиться ProgressDialog із повідомленням "Збереження...". Це дає користувачеві відчуття того, що додаток працює над операцією.

4. **Питання зберігається в базі даних:**
   Введене користувачем питання про здоров'я зберігається в Firebase Realtime Database під шляхом "HealthQuestion". У базі даних створюється новий запис із унікальним ідентифікатором (ключем), самим питанням та датою створення.

5. **ProgressDialog закривається, і виводиться результат:**
   Після завершення збереження виводиться результат у діалоговому вікні. Якщо операція успішна, користувач отримує повідомлення "Питання успішно збережено". У випадку помилки виводиться повідомлення про помилку.

6. **Користувач може ввести нове питання або повторити операцію:**
   Після завершення операції користувач може ввести нове питання або повторити процес надсилання питань.

Отже, метод `saveHealthQuestion` реалізує операцію збереження питань про здоров'я, а приклад його використання - це додавання нових питань користувачами за допомогою введення та відправлення їх через додаток.