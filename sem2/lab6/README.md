# Лабораторная работа №6. Установка и настройка IntelliJ IDEA для разработки приложений под Android

**Цель работы:** установить и настроить среду разработки IntelliJ IDEA для
создания приложений под Android, настроить SDK и эмулятор, проверить
работоспособность проекта.

## 1. Установка IntelliJ IDEA

1. Загрузить дистрибутив с сайта разработчика:
   <https://www.jetbrains.com/ru-ru/idea/download/other.html>.
   Доступны две редакции: IntelliJ IDEA Ultimate (платная, пробный период
   30 дней) и IntelliJ IDEA Community Edition (бесплатная). Для работы с
   Android используется Community Edition — в ней плагин Android доступен без
   ограничений.
2. Установить IDE в соответствии с версией операционной системы
   (установщик `.exe` для Windows).
3. Запустить IDE и открыть настройки комбинацией `Ctrl+Alt+S`.
4. В поиске настроек ввести «Android» и на вкладке **Marketplace** установить
   плагины **Android** и **Android Design Tools**.
5. Перезапустить IDE кнопкой **Restart IDE**.

## 2. Установка Android SDK

1. Создать новый проект (**File → New → Project**) и выбрать шаблон
   **Empty Views Activity**.
2. Выбрать версию SDK: от неё зависит, на каких устройствах будет
   запускаться приложение (в работе используется API 34, минимальная
   поддерживаемая версия — API 24).
3. IDE предложит загрузить компоненты SDK — согласиться, после чего
   скачиваются Platform, Build-Tools и Platform-Tools.

Дополнительные версии SDK устанавливаются через
**Settings → Languages & Frameworks → Android SDK**: выбрать нужную версию и
нажать **Apply**. После установки SDK подключается к проекту в
**File → Project Structure → Project → SDK → Add SDK → Android SDK**, где
указывается каталог SDK (по умолчанию
`C:\Users\<имя_пользователя>\AppData\Local\Android\Sdk`).

## 3. Установка эмулятора

1. Открыть вкладку **Device Manager**.
2. Нажать **Create Virtual Device**, выбрать модель устройства (например,
   Pixel 5) и нажать **Next**.
3. Выбрать образ системы, совместимый с используемым SDK
   (`Android 14 / API 34, Google APIs, x86_64`), загрузить его.
4. Завершить настройку и запустить виртуальное устройство.
5. Запустить приложение (**Run 'app'**). Если на экране эмулятора появилось
   приложение с надписью «Hello World!», настройка выполнена верно.

## 4. Проект, созданный по шаблону Empty Views Activity

В каталоге `android-app` размещён проект, созданный по тому же шаблону, что
предлагает IDE, и собранный без графической среды — командной строкой:

```
android-app/
├── settings.gradle        репозитории Google и Maven Central, модуль :app
├── build.gradle           плагин com.android.application 8.5.2
├── gradle.properties      AndroidX, отключение проверки пути (кириллица)
├── local.properties       путь к Android SDK
└── app/
    ├── build.gradle       compileSdk 34, minSdk 24, targetSdk 34
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/ru/belgu/cad/lab6/MainActivity.java
        └── res/layout/activity_main.xml, res/values/{strings,themes}.xml
```

Приложение выводит приветствие «Hello World!», сведения об API и модели
устройства, а также кнопку, увеличивающую счётчик нажатий — это проверяет, что
проект собран и запускается, а обработчики событий работают:

```java
public class MainActivity extends AppCompatActivity {

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView counterView = findViewById(R.id.counterText);
        TextView sdkView = findViewById(R.id.sdkText);
        Button button = findViewById(R.id.countButton);

        sdkView.setText(getString(R.string.sdk_info,
                android.os.Build.VERSION.SDK_INT, android.os.Build.MODEL));

        button.setOnClickListener(v -> {
            counter++;
            counterView.setText(getString(R.string.counter_value, counter));
        });
    }
}
```

## 5. Результаты работы

Установленные компоненты и версии:

| Компонент | Версия / путь |
|---|---|
| JDK | Eclipse Temurin 17.0.21 |
| Gradle | 8.7 |
| Android Gradle Plugin | 8.5.2 |
| Android SDK | `C:\Users\shizus\tools\android\sdk` |
| Platform | Android 14 (API 34) |
| Build-Tools | 34.0.0 |
| Platform-Tools | установлены |

Сборка проекта:

```
BUILD SUCCESSFUL in 46s
33 actionable tasks: 33 executed
```

Полученный пакет `app/build/outputs/apk/debug/app-debug.apk` (5 579 715 байт)
проверен утилитой `aapt2 dump badging`:

```
package: name='ru.belgu.cad.lab6' versionCode='1' versionName='1.0'
         compileSdkVersion='34' compileSdkVersionCodename='14'
sdkVersion:'24'
targetSdkVersion:'34'
application-label:'ЛР6 Android'
```

| № | Проверка | Результат |
|---|---|---|
| 1 | Плагины Android и Android Design Tools | устанавливаются через Marketplace (см. раздел 1) |
| 2 | SDK Platform 34, Build-Tools 34.0.0, Platform-Tools | загружены, лицензии приняты |
| 3 | Создание проекта Empty Views Activity | проект `android-app` создан, структура соответствует шаблону |
| 4 | Сборка проекта | `BUILD SUCCESSFUL`, 33 задачи выполнено |
| 5 | Проверка APK | пакет `ru.belgu.cad.lab6`, minSdk 24, targetSdk 34, метка «ЛР6 Android» |
| 6 | Виртуальное устройство Pixel 5, Android 14 (API 34, Google APIs, x86_64) | AVD создан, эмулятор загрузился (`sys.boot_completed=1`) |
| 7 | Установка и запуск приложения на эмуляторе | `adb install` — `Success`, активность `MainActivity` запущена |
| 8 | Проверка интерфейса на эмуляторе | на экране: «ЛР6 Android», «Hello World!», «API 34, устройство sdk_gphone64_x86_64», «Нажатий: 0», кнопка «НАЖАТЬ» |
| 9 | Проверка обработчика нажатий | три нажатия по кнопке → счётчик изменился на «Нажатий: 3» |

Дерево интерфейса, полученное с эмулятора (`uiautomator dump`), после трёх
нажатий по кнопке:

```
ЛР6 Android
Hello World!
API 34, устройство sdk_gphone64_x86_64
Нажатий: 3
НАЖАТЬ
```

Снимок экрана эмулятора с работающим приложением — `agents/lab6_emulator.png`.

## 6. Выводы

Изучен порядок установки и настройки IntelliJ IDEA для разработки под Android:
установка IDE, подключение плагинов Android и Android Design Tools, установка
и подключение Android SDK, создание проекта по шаблону Empty Views Activity,
настройка эмулятора и запуск приложения. Проект, созданный по шаблону,
успешно собран командной строкой (Gradle 8.7 + Android Gradle Plugin 8.5.2,
compileSdk 34), полученный APK проверен утилитой `aapt2` и содержит корректные
имя пакета, версии SDK и метку приложения. Приложение установлено на
виртуальное устройство Pixel 5 (Android 14, API 34) и запущено: на экране
эмулятора отображается приветствие, сведения о версии API и модели устройства,
а обработчик нажатия кнопки корректно увеличивает счётчик. Дополнительно
настроены дополнительные версии SDK через `sdkmanager`, что соответствует
шагам методических указаний.

## 7. Сборка и запуск из командной строки

```bash
export ANDROID_HOME=/c/Users/shizus/tools/android/sdk

# сборка пакета
cd android-app
/c/Users/shizus/tools/android/gradle-8.7/bin/gradle.bat --no-daemon assembleDebug
# результат: app/build/outputs/apk/debug/app-debug.apk

# создание и запуск виртуального устройства
avdmanager create avd -n lab6 -k "system-images;android-34;google_apis;x86_64" -d pixel_5
emulator -avd lab6 -no-window -no-audio -gpu swiftshader_indirect

# установка и запуск приложения
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n ru.belgu.cad.lab6/.MainActivity
```
