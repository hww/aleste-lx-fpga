## Рекомендуемые расширения VS Code

Установите следующие расширения:

- Scala (Metals) - для работы с Scala
- Verilog-HDL/SystemVerilog - для работы с RTL
- Makefile Tools - для интеграции Makefile
- Yosys - для подсветки синтеза

## Зависимости

Убедитесь, что у вас установлены:

    Yosys (с поддержкой ECP5)

    NextPNR-ECP5

    ecppack (из проекта Trellis)

Для Ubuntu/Debian:
bash

sudo apt install yosys nextpnr-ecp5 trellis-tools

## Документация

В Scala (и, соответственно, в SpinalHDL) для документирования кода используются Scaladoc-комментарии - аналог Javadoc для Scala. Вот как правильно оформлять документацию для генерации автодокументации (например, с помощью cbt, sbt-doc или других инструментов):
1. Базовый Scaladoc

```scala
/**
  * Класс ЦАП с ШИМ-модуляцией.
  *
  * @param bitWidth           Разрядность ЦАП (количество бит)
  * @param clockDividerWidth  Ширина счётчика делителя частоты (0 для отключения)
  */
case class PwmDac(bitWidth: Int, clockDividerWidth: Int = 0) extends Component {
  ...
}
```

2. Подробная документация с примерами

```scala
/**
  * Дельта-сигма ЦАП с высоким разрешением.
  *
  * ==Принцип работы==
  * Использует модуляцию 1-го порядка для переноса шума квантования в ВЧ-область.
  *
  * ==Пример использования==
  * {{{
  * val dac = DeltaSigmaDac(16)
  * dac.io.value := audioSample
  * io.audioOut := dac.io.dacOut
  * }}}
  *
  * @param bitWidth            Разрядность входных данных (16-24 бита)
  * @param oversamplingRatio   Коэффициент передискретизации (>=64)
  * @see [[PwmDac]] Для простой ШИМ-реализации
  * @note Для аналоговой части требуется RC-фильтр 1-го порядка
  */
case class DeltaSigmaDac(bitWidth: Int, oversamplingRatio: Int = 64) extends Component {
  ...
}
```

3. Документирование полей и методов

```scala
class AudioController extends Component {
  /**
    * Конфигурация аудиоформата.
    * - 0: 16 бит, 44.1 кГц
    * - 1: 24 бит, 96 кГц
    */
  val config = in UInt(2 bits)

  /**
    * Выходной аудиопоток.
    * @return 1-битный дельта-сигма поток
    */
  def audioOut(): Bool = {
    ...
  }
}
```
4. Специальные теги Scaladoc

Тег	Описание

- @param	Описание параметра
- @return	Описание возвращаемого значения
- @throws	Исключения, которые может выбросить метод
- @see	Ссылки на связанные классы
- @note	Важные замечания
- @example	Пример использования (альтернатива {{{}}})
- @deprecated	Пометка устаревшего кода

5. Генерация документации

    sbt:
    bash

sbt doc

Документация появится в target/scala-<version>/api/

cbt:

Добавьте в build/build.scala:

```scala
override def doc = super.doc ++ Seq(
  scalacOptions in Compile in doc ++= Seq(
    "-doc-title", "My Project",
    "-doc-version", "1.0"
  )
)
```

Затем:

```bash
    cbt doc
```

6. SpinalHDL-специфика

Для документирования аппаратных интерфейсов:

```scala
class SpiController extends Component {
  /** SPI-интерфейс.
    * @master Режим ведущего (контроллер)
    */
  val spi = master(Spi())
}
```

Итоговый пример для SpinalHDL-проекта

```scala
/**
  * ЦАП с ШИМ для FPGA.
  *
  * @param bitWidth  Разрядность (8-12 бит)
  * @param pwmFreq  Частота ШИМ (по умолчанию 100 кГц)
  * @group Audio
  * @see [[DeltaSigmaDac]] Для высококачественного аудио
  */
case class PwmDac(bitWidth: Int, pwmFreq: HertzNumber = 100 kHz) extends Component {
  require(bitWidth >= 8 && bitWidth <= 12, "Supported resolution: 8-12 bits")

  /** Входное значение ЦАП.
    * @note Обновляется по переднему фронту clock
    */
  val value = in UInt(bitWidth bits)

  /** Выход ШИМ.
    * @warning Требует внешнего RC-фильтра!
    */
  val pwmOut = out Bool()
}
```
Для генерации документации в проектах SpinalHDL рекомендуется использовать sbt-microsites или mdoc для интеграции с Markdown.