package com.example.challengefit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.challengefit.databinding.ActivityMainBinding;
import com.example.challengefit.request.ApiClient;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment_activity_main);
            
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                
                BottomNavigationView navView = binding.navView;
                
                navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                    // OCULTAR MENÚ EN LOGIN Y REGISTRO
                    if (destination.getId() == R.id.navigation_login || destination.getId() == R.id.navigation_registro) {
                        navView.setVisibility(View.GONE);
                        if (getSupportActionBar() != null) getSupportActionBar().hide();
                    } else {
                        navView.setVisibility(View.VISIBLE);
                        if (getSupportActionBar() != null) getSupportActionBar().show();
                    }
                });
                
                NavigationUI.setupWithNavController(binding.navView, navController);

                navView.setOnItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.navigation_logout) {
                        confirmarCierreSesion();
                        return true;
                    }
                    return NavigationUI.onNavDestinationSelected(item, navController);
                });

                String token = ApiClient.leerToken(this);
                String rol = ApiClient.leerRol(this);
                if (token != null && rol != null) {
                    binding.getRoot().post(() -> setupNavigationByRole(rol));
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error en onCreate: " + e.getMessage());
        }
    }

    private void confirmarCierreSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Desea cerrar la sesión de ChallengeFit?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    ApiClient.cerrarSesion(MainActivity.this);
                    navController.navigate(R.id.navigation_login);
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void setupNavigationByRole(String userType) {
        if (navController == null) return;

        try {
            BottomNavigationView navView = binding.navView;
            navView.getMenu().clear();
            int targetDestination;
            AppBarConfiguration appBarConfiguration;

            if ("2".equals(userType) || "Entrenador".equals(userType)) {
                navView.inflateMenu(R.menu.menu_trainer);
                targetDestination = R.id.navigation_routines_trainer;
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_routines_trainer, R.id.navigation_students_trainer, R.id.navigation_challenges_trainer)
                        .build();
            } else {
                navView.inflateMenu(R.menu.menu_student);
                targetDestination = R.id.navigation_home_student;
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home_student, R.id.navigation_challenges_student, R.id.navigation_progress_student)
                        .build();
            }

            navView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.navigation_logout) {
                    confirmarCierreSesion();
                    return true;
                }
                return NavigationUI.onNavDestinationSelected(item, navController);
            });

            if (getSupportActionBar() != null) {
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            }
            
            navController.navigate(targetDestination);
            
        } catch (Exception e) {
            Log.e("MainActivity", "Error en setupNavigation: " + e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null) {
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}
