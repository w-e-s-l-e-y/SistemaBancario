package org.example;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class RealtimeDatabase {
	private FirebaseDatabase database;

	public RealtimeDatabase() {
		// Verifica se o FirebaseApp já foi inicializado
		if (FirebaseApp.getApps().isEmpty()) {
			try {
				FileInputStream serviceAccount = new FileInputStream("config/firebase-adminsdk.json");
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://wykbank-5b21d-default-rtdb.firebaseio.com/")
						.build();
				FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		database = FirebaseDatabase.getInstance();
	}

	public void setValue(String path, Object value) {
		DatabaseReference ref = database.getReference(path);
		ref.setValueAsync(value);
	}

	public int getLastAccountNumber() throws InterruptedException {
		DatabaseReference ref = database.getReference("correntistas");
		final int[] lastAccountNumber = {0};
		CountDownLatch latch = new CountDownLatch(1);

		ref.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					lastAccountNumber[0] = Integer.parseInt(childSnapshot.getKey());
				}
				latch.countDown();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				latch.countDown();
			}
		});

		latch.await();
		return lastAccountNumber[0];
	}

	public boolean validarLogin(String nome, int numeroConta) throws InterruptedException {
		DatabaseReference ref = database.getReference("correntistas/" + numeroConta);
		final boolean[] isValid = {false};
		CountDownLatch latch = new CountDownLatch(1);

		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					String nomeFirebase = dataSnapshot.child("nome").getValue(String.class);
					if (nome != null && nome.equals(nomeFirebase)) {
						isValid[0] = true;
					}
				}
				latch.countDown();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				latch.countDown();
			}
		});

		latch.await();
		return isValid[0];
	}

	public void atualizarSaldoBancoDados(int id, double saldo, double chequeEspecial) {
		DatabaseReference ref = database.getReference("correntistas/" + id);
		ref.child("saldo").setValueAsync(saldo);
		ref.child("chequeEspecial").setValueAsync(chequeEspecial);
	}

	public ContaCorrente getContaCorrente(int numeroConta) throws InterruptedException {
		DatabaseReference ref = database.getReference("correntistas/" + numeroConta);
		final ContaCorrente[] contaCorrente = {null};
		CountDownLatch latch = new CountDownLatch(1);

		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					double saldo = dataSnapshot.child("saldo").getValue(Double.class);
					double chequeEspecial = dataSnapshot.child("cheque_Especial").getValue(Double.class);
					boolean ativa = dataSnapshot.child("ativo").getValue(Boolean.class);
					contaCorrente[0] = new ContaCorrente(numeroConta, saldo, ativa);
					contaCorrente[0].setChequeEspecial(chequeEspecial);
				}
				latch.countDown();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				latch.countDown();
			}
		});

		latch.await();
		return contaCorrente[0];
	}
}
