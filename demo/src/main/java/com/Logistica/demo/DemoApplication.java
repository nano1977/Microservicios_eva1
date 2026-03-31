@Bean
CommandLineRunner initDatabase(VehiculoRepository repository) {
    return args -> {
        Vehiculo v1 = new Vehiculo();
        v1.setPatente("HELP-01");
        v1.setModelo("Camión Frigorífico (Insumos Médicos)");
        v1.setChofer("Juan Pérez");
        v1.setCapacidadCarga(5000.0);
        v1.setEstado("Disponible");

        repository.save(v1);
        System.out.println(">>> Sistema Donaton: Vehículo logístico inicializado.");
    };
}