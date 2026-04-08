package com.example.challengefit.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.challengefit.MainActivity;
import com.example.challengefit.R;
import com.example.challengefit.databinding.FragmentLoginBinding;
import com.example.challengefit.request.ApiClient;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View Root = binding.getRoot();

        // NAVEGACIÓN AL REGISTRO
        binding.linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_registro);
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.inputEmail.getText().toString();
                String pass = binding.inputPassword.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty()){
                    mViewModel.login(email, pass);
                } else {
                    binding.tvMensaje2.setVisibility(View.VISIBLE);
                    binding.tvMensaje2.setText("Complete todos los campos");
                }
            }
        });

        mViewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvMensaje2.setVisibility(View.VISIBLE);
                binding.tvMensaje2.setText(s);
            }
        });

        mViewModel.getLoginExitoso().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    String rol = ApiClient.leerRol(requireContext());
                    binding.getRoot().post(() -> {
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).setupNavigationByRole(rol);
                        }
                    });
                }
            }
        });

        return Root;
    }
}
