package top.wavelength.betterreflection.dumper.implementation;

import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.TypeDisplayNameFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * The MethodDumper class is responsible for dumping information related to a field.
 * It extends the Dumper class and operates on an object of type {@link Field}.
 * It provides methods to dump the field's information in a formatted string representation.
 *
 * @since 1.2
 */
public class FieldDumper implements Dumper<Field> {

	private boolean includeModifiers = true;
	private TypeDisplayNameFormat typeDisplay = TypeDisplayNameFormat.FULL_NAME;
	private boolean includeName = true;

	/**
	 * Returns whether to include modifiers in the dumped string representation of a Field object.
	 *
	 * @return true if modifiers should be included, false otherwise
	 * @since 1.2
	 */
	public boolean isIncludeModifiers() {
		return includeModifiers;
	}

	/**
	 * Sets whether to include modifiers in the dumped string representation of a Field object.
	 *
	 * @param includeModifiers true if modifiers should be included, false otherwise
	 * @return the FieldDumper instance
	 * @since 1.2
	 */
	public FieldDumper setIncludeModifiers(boolean includeModifiers) {
		this.includeModifiers = includeModifiers;
		return this;
	}

	/**
	 * Gets the display format for the type of the Field.
	 *
	 * @return the display format for the Field's type
	 * @since 1.2
	 */
	public TypeDisplayNameFormat getTypeDisplay() {
		return typeDisplay;
	}

	/**
	 * Sets the display format for the return type of the Field.
	 *
	 * @param typeDisplay the display format for the type
	 * @return the FieldDumper instance
	 * @since 1.2
	 */
	public FieldDumper setTypeDisplay(TypeDisplayNameFormat typeDisplay) {
		this.typeDisplay = typeDisplay;
		return this;
	}

	/**
	 * Returns whether to include the name in the dumped string representation of a Field object.
	 *
	 * @return true if the name should be included, false otherwise
	 * @since 1.2
	 */
	public boolean isIncludeName() {
		return includeName;
	}

	/**
	 * Sets whether to include the name in the dumped string representation of a Field object.
	 *
	 * @param includeName true if the name should be included, false otherwise
	 * @return the FieldDumper instance
	 * @since 1.2
	 */
	public FieldDumper setIncludeName(boolean includeName) {
		this.includeName = includeName;
		return this;
	}

	/**
	 * Returns a formatted string representation of a Field.
	 *
	 * @param field The Field to dump
	 * @return The formatted string representation of the Field
	 * @since 1.2
	 */
	@Override
	public String dump(Field field) {
		return String.format("%s%s%s", includeModifiers ? Modifier.toString(field.getModifiers()) + " " : "",
				typeDisplay != TypeDisplayNameFormat.NONE ? typeDisplay.getName(field) + " " : "", // TODO: Only append space if needed.
				includeName ? field.getName() : "");
	}
}