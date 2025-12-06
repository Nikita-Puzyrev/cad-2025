package com.musicstore.config;

import com.musicstore.model.Category;
import com.musicstore.model.Equipment;
import com.musicstore.repository.CategoryRepository;
import com.musicstore.repository.EquipmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    
    @Bean
    @Transactional
    public CommandLineRunner initData(CategoryRepository categoryRepository,
                                     EquipmentRepository equipmentRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                System.out.println("🔄 Инициализация базы данных...");
                
                // Создаем категории
                Category guitars = new Category("Гитары", "Акустические и электрические гитары");
                Category keyboards = new Category("Клавишные", "Синтезаторы, цифровые пианино");
                Category drums = new Category("Ударные", "Барабанные установки и перкуссия");
                Category amps = new Category("Усилители", "Гитарные и басовые усилители");
                Category microphones = new Category("Микрофоны", "Вокальные и инструментальные микрофоны");
                Category audio = new Category("Аудио", "Аудиоинтерфейсы, мониторы, процессоры");
                
                categoryRepository.save(guitars);
                categoryRepository.save(keyboards);
                categoryRepository.save(drums);
                categoryRepository.save(amps);
                categoryRepository.save(microphones);
                categoryRepository.save(audio);
                
                // Создаем оборудование
                Equipment eq1 = new Equipment("Fender Stratocaster", 
                    "Электрогитара", "Fender", 1200.00, 5, guitars, 
                    "Классическая электрогитара американского производства");
                eq1.setAddedDate(LocalDateTime.now().minusDays(10));
                
                Equipment eq2 = new Equipment("Yamaha P-125", 
                    "Цифровое пианино", "Yamaha", 800.00, 3, keyboards, 
                    "Портативное цифровое пианино с 88 взвешенными клавишами");
                eq2.setAddedDate(LocalDateTime.now().minusDays(8));
                
                Equipment eq3 = new Equipment("Pearl Export", 
                    "Барабанная установка", "Pearl", 1500.00, 2, drums, 
                    "Комплект для начинающих барабанщиков");
                eq3.setAddedDate(LocalDateTime.now().minusDays(5));
                
                Equipment eq4 = new Equipment("Marshall DSL40CR", 
                    "Гитарный комбоусилитель", "Marshall", 900.00, 4, amps, 
                    "Трубный усилитель 40Вт с кабинетом Celestion");
                eq4.setAddedDate(LocalDateTime.now().minusDays(3));
                
                Equipment eq5 = new Equipment("Shure SM58", 
                    "Динамический микрофон", "Shure", 100.00, 15, microphones, 
                    "Легендарный вокальный микрофон");
                eq5.setAddedDate(LocalDateTime.now().minusDays(1));
                
                equipmentRepository.save(eq1);
                equipmentRepository.save(eq2);
                equipmentRepository.save(eq3);
                equipmentRepository.save(eq4);
                equipmentRepository.save(eq5);
                
                System.out.println("База данных инициализирована с тестовыми данными");
                System.out.println("Создано категорий: " + categoryRepository.count());
                System.out.println("Создано товаров: " + equipmentRepository.count());
            } else {
                System.out.println("База данных уже содержит данные");
                System.out.println("Категорий: " + categoryRepository.count());
                System.out.println("Товаров: " + equipmentRepository.count());
                System.out.println("Общая стоимость инвентаря: $" + 
                    String.format("%.2f", equipmentRepository.calculateTotalValue()));
            }
        };
    }
}